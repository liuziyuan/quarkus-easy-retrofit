package io.quarkiverse.easy.retrofit.client.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkiverse.easy.retrofit.client.runtime.*;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.deployment.*;
import io.quarkus.arc.processor.BeanInfo;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public final class RetrofitClientProcessor {

    private static final Logger LOG = Logger.getLogger(RetrofitClientProcessor.class);
    private static final String FEATURE = "retrofit-client";

    static DotName ENABLE_RETROFIT_ANNOTATION = DotName.createSimple(EnableRetrofit.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(STATIC_INIT)
    void scanRetrofitResource(RetrofitRecorder recorder,
            BeanArchiveIndexBuildItem beanArchiveIndex,
            BuildProducer<EnableRetrofitBuildItem> producer) {
        IndexView indexView = beanArchiveIndex.getIndex();

        Collection<AnnotationInstance> annotations = indexView.getAnnotations(ENABLE_RETROFIT_ANNOTATION);
        if (annotations.size() > 1) {
            throw new RuntimeException("@EnableRetrofit can only be used once");
        }
        if (annotations.size() == 1) {
            AnnotationInstance annotationInstance = annotations.iterator().next();

            EnableRetrofitBean enableRetrofit = new EnableRetrofitBean();
            enableRetrofit.setValue(annotationInstance.value("value") == null ? new String[0]
                    : annotationInstance.value("value").asStringArray());
            enableRetrofit.setBasePackages(annotationInstance.value("basePackages") == null ? new String[0]
                    : annotationInstance.value("basePackages").asStringArray());
            enableRetrofit.setBasePackageClasses(
                    getBasePackageClasses(annotationInstance.value("basePackageClasses") == null ? new Type[0]
                            : annotationInstance.value("basePackageClasses").asClassArray()));
            RetrofitAnnotationBeanRegister retrofitAnnotationBeanRegister = new RetrofitAnnotationBeanRegister();
            Set<Class<?>> retrofitResource = retrofitAnnotationBeanRegister.scanRetrofitResource(enableRetrofit);
            List<String> basePackages = retrofitAnnotationBeanRegister.getBasePackages(enableRetrofit);
            RetrofitAnnotationBean retrofitAnnotationBean = new RetrofitAnnotationBean(basePackages, retrofitResource);
            EnableRetrofitBuildItem enableRetrofitBuildItem = new EnableRetrofitBuildItem(retrofitAnnotationBean);
            producer.produce(enableRetrofitBuildItem);
        }
    }

    @BuildStep
    void getRetrofitBuilderExtension(
            BeanDiscoveryFinishedBuildItem beanDiscovery,
            BuildProducer<RetrofitBuilderExtensionBuildItem> producer) throws ClassNotFoundException {
        Collection<BeanInfo> beans = beanDiscovery.getBeans();
        for (BeanInfo bean : beans) {
            List<Type> collect = bean.getTypes().stream()
                    .filter(type -> type.name().toString().equals(RetrofitBuilderExtension.class.getName()))
                    .collect(Collectors.toList());
            if (collect.size() > 1) {
                LOG.warn("There are multiple " + RetrofitBuilderExtension.class.getSimpleName()
                        + ", please check your configuration");
            } else if (collect.size() == 1) {
                String className = bean.getImplClazz().name().toString();
                producer.produce(
                        new RetrofitBuilderExtensionBuildItem(className));
            }
        }
    }

    @BuildStep
    void getRetrofitInterceptorExtension(
            BeanDiscoveryFinishedBuildItem beanDiscovery,
            BuildProducer<RetrofitInterceptorExtensionBuildItem> producer) throws ClassNotFoundException {
        Collection<BeanInfo> beans = beanDiscovery.getBeans();
        List<String> classNames = new ArrayList<>();
        for (BeanInfo bean : beans) {
            List<Type> collect = bean.getTypes().stream()
                    .filter(type -> type.name().toString().equals(RetrofitInterceptorExtension.class.getName()))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                String className = bean.getImplClazz().name().toString();
                classNames.add(className);
            }
        }
        producer.produce(
                new RetrofitInterceptorExtensionBuildItem(classNames));
    }

    @BuildStep
    @Record(RUNTIME_INIT)
    void registerRetrofitResource(
            RetrofitRecorder recorder,
            EnableRetrofitBuildItem enableRetrofitBuildItem,
            RetrofitBuilderExtensionBuildItem retrofitBuilderExtensionBuildItem,
            RetrofitInterceptorExtensionBuildItem retrofitInterceptorExtensionBuildItem,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer,
            RetrofitBuilderGlobalConfigProperties globalConfigProperties) throws ClassNotFoundException {
        if (enableRetrofitBuildItem != null) {
            Class<? extends RetrofitBuilderExtension> retrofitBuilderExtensionClass = (Class<? extends RetrofitBuilderExtension>) retrofitBuilderExtensionBuildItem
                    .getRetrofitBuilderExtensionClass();
            List<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorExtensionClasses = retrofitInterceptorExtensionBuildItem
                    .getRetrofitInterceptorExtensionClasses();
            //            RetrofitBuilderExtensionRegister retrofitBuilderExtensionRegister = new RetrofitBuilderExtensionRegister();
            //            RetrofitBuilderGlobalConfig globalConfig = retrofitBuilderExtensionRegister.getGlobalConfig(globalConfigProperties,
            //                    null);
            //            RetrofitResourceContextRegister retrofitResourceContextRegister = new RetrofitResourceContextRegister();
            //            RetrofitResourceContext context = retrofitResourceContextRegister.getContext(
            //                    enableRetrofitBuildItem.getRetrofitAnnotationBean(),
            //                    globalConfig, null);
            //            String[] basePackages = context.getBasePackages();

            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                    .configure(RetrofitResourceContext.class)
                    .setRuntimeInit()
                    .scope(ApplicationScoped.class)
                    .unremovable()
                    .runtimeValue(recorder.createRetrofitResourceContext(
                            enableRetrofitBuildItem.getRetrofitAnnotationBean(), globalConfigProperties,
                            retrofitBuilderExtensionClass, retrofitInterceptorExtensionClasses));
            syntheticBeanBuildItemBuildProducer.produce(configurator.done());
        }
    }

    private Class<?>[] getBasePackageClasses(Type[] type) {
        Class<?>[] classes = new Class[type.length];
        for (int i = 0; i < type.length; i++) {
            classes[i] = (Class<?>) type[i].getClass();
        }
        return classes;
    }

}
