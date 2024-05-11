package io.quarkiverse.easy.retrofit.client.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import io.github.liuziyuan.retrofit.core.*;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.quarkiverse.easy.retrofit.client.runtime.*;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.deployment.*;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import retrofit2.Retrofit;

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
    void registerRetrofitResource(RetrofitRecorder recorder,
            BeanArchiveIndexBuildItem beanArchiveIndex,
            RetrofitBuilderGlobalConfigProperties globalConfigProperties,
            BuildProducer<RetrofitResourceContextBuildItem> producer,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) {
        IndexView indexView = beanArchiveIndex.getIndex();
        Collection<AnnotationInstance> annotations = indexView.getAnnotations(ENABLE_RETROFIT_ANNOTATION);
        if (annotations.size() > 1) {
            throw new RuntimeException("@EnableRetrofit can only be used once");
        }
        if (annotations.size() == 1) {
            AnnotationInstance annotationInstance = annotations.iterator().next();

            EnableRetrofitBean enableRetrofitBean = new EnableRetrofitBean();
            enableRetrofitBean.setValue(annotationInstance.value("value") == null ? new String[0]
                    : annotationInstance.value("value").asStringArray());
            enableRetrofitBean.setBasePackages(annotationInstance.value("basePackages") == null ? new String[0]
                    : annotationInstance.value("basePackages").asStringArray());
            enableRetrofitBean.setBasePackageClasses(
                    getBasePackageClasses(annotationInstance.value("basePackageClasses") == null ? new Type[0]
                            : annotationInstance.value("basePackageClasses").asClassArray()));
            enableRetrofitBean.setExtensionPackages(annotationInstance.value("extensionPackages") == null ? new String[0]
                    : annotationInstance.value("extensionPackages").asStringArray());
            enableRetrofitBean.setExtensionPackageClasses(
                    getBasePackageClasses(annotationInstance.value("extensionPackageClasses") == null ? new Type[0]
                            : annotationInstance.value("extensionPackageClasses").asClassArray()));
            // get retrofitAnnotationBean
            RetrofitAnnotationBeanRegister retrofitAnnotationBeanRegister = new RetrofitAnnotationBeanRegister();
            Set<Class<?>> retrofitResource = retrofitAnnotationBeanRegister.scanRetrofitResource(enableRetrofitBean);
            List<String> basePackages = retrofitAnnotationBeanRegister.getBasePackages(enableRetrofitBean);
            RetrofitAnnotationBean retrofitAnnotationBean = new RetrofitAnnotationBean(basePackages, retrofitResource);
            // get retrofitExtension
            RetrofitExtensionRegister retrofitExtensionRegister = new RetrofitExtensionRegister();
            RetrofitResourceScanner.RetrofitExtension retrofitExtension = retrofitExtensionRegister
                    .scanRetrofitExtension(enableRetrofitBean);
            RetrofitBuilderExtension retrofitBuilderExtension = retrofitExtensionRegister
                    .getRetrofitBuilderExtension(retrofitExtension.getRetrofitBuilderClasses());
            // get interceptorExtensions
            List<RetrofitInterceptorExtension> retrofitInterceptorExtensions = retrofitExtensionRegister
                    .getRetrofitInterceptorExtensions(retrofitExtension.getRetrofitInterceptorClasses());
            //get globalConfig(BuilderExtension)
            RetrofitBuilderExtensionRegister retrofitBuilderExtensionRegister = new RetrofitBuilderExtensionRegister();
            RetrofitBuilderGlobalConfig globalConfig = retrofitBuilderExtensionRegister.getGlobalConfig(globalConfigProperties,
                    retrofitBuilderExtension);
            // create RetrofitResourceContext
            Env env = new QuarkusEnv();
            RetrofitResourceContextBuilder contextBuilder = new RetrofitResourceContextBuilder(env);
            RetrofitResourceContext retrofitResourceContext = contextBuilder.buildContextInstance(
                    retrofitAnnotationBean.getBasePackages().toArray(new String[0]),
                    retrofitAnnotationBean.getRetrofitBuilderClassSet(),
                    globalConfig,
                    retrofitInterceptorExtensions);

            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                    .configure(RetrofitResourceContext.class)
                    .scope(Singleton.class)
                    .unremovable()
                    .runtimeValue(recorder.getRetrofitResourceContextInstance(retrofitResourceContext));
            syntheticBeanBuildItemBuildProducer.produce(configurator.done());

            producer.produce(new RetrofitResourceContextBuildItem(retrofitResourceContext));
            //            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
            //                    .configure(RetrofitContext.class)
            //                    .scope(Singleton.class)
            //                    .unremovable()
            //                    .runtimeValue(recorder.getRetrofitContextInstance(retrofitResourceContext));
            //            syntheticBeanBuildItemBuildProducer.produce(configurator.done());
        }
    }

    @BuildStep
    @Record(RUNTIME_INIT)
    void registerRetrofitResource(
            RetrofitRecorder recorder,
            RetrofitResourceContextBuildItem retrofitResourceContextBuildItem,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) {
        if (retrofitResourceContextBuildItem != null) {
            RetrofitResourceContext context = retrofitResourceContextBuildItem.getContext();
            for (RetrofitClientBean clientBean : context.getRetrofitClients()) {
                SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                        .configure(Retrofit.class)
                        .setRuntimeInit()
                        .scope(ApplicationScoped.class)
                        .unremovable().runtimeValue(recorder.getRetrofitInstance(clientBean, context))
                        .addQualifier().annotation(Named.class).addValue("value", clientBean.getRetrofitInstanceName()).done();
                syntheticBeanBuildItemBuildProducer.produce(configurator.done());
            }
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
