package io.quarkiverse.easy.retrofit.client.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import io.quarkiverse.easy.retrofit.client.runtime.*;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.deployment.*;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.runtime.RuntimeValue;

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
            List<RetrofitInterceptorExtension> retrofitInterceptorExtensions = retrofitExtensionRegister
                    .getRetrofitInterceptorExtensions(retrofitExtension.getRetrofitInterceptorClasses());
            // create RetrofitResourceContext
            RetrofitBuilderExtensionRegister retrofitBuilderExtensionRegister = new RetrofitBuilderExtensionRegister();
            RetrofitBuilderGlobalConfig globalConfig = retrofitBuilderExtensionRegister.getGlobalConfig(globalConfigProperties,
                    retrofitBuilderExtension);

            RetrofitResourceContextRegister retrofitResourceContextRegister = new RetrofitResourceContextRegister();
            RetrofitResourceContext context = retrofitResourceContextRegister.getContext(retrofitAnnotationBean,
                    globalConfig, retrofitInterceptorExtensions);
            RuntimeValue<RetrofitResourceContext> runtimeValue = recorder.createRetrofitResourceContext(context);
            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                    .configure(RetrofitResourceContext.class)
                    .scope(ApplicationScoped.class)
                    .unremovable()
                    .supplier(recorder.getRetrofitResourceContextSupplier(runtimeValue));

            syntheticBeanBuildItemBuildProducer.produce(configurator.done());

        }
    }

    //    @BuildStep
    //    @Record(RUNTIME_INIT)
    //    void registerRetrofitResource(
    //            RetrofitRecorder recorder,
    //            RetrofitResourceContextBuildItem retrofitResourceContextBuildItem,
    //            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) {
    //        if (retrofitResourceContextBuildItem != null) {
    //            //            RetrofitResourceContext context = retrofitResourceContextBuildItem.getContext();
    //            //            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
    //            //                    .configure(RetrofitResourceContext.class)
    //            //                    .setRuntimeInit()
    //            //                    .scope(ApplicationScoped.class)
    //            //                    .unremovable()
    //            //                    .runtimeValue(recorder.createRetrofitResourceContext(context));
    //            //            syntheticBeanBuildItemBuildProducer.produce(configurator.done());
    //        }
    //    }

    private Class<?>[] getBasePackageClasses(Type[] type) {
        Class<?>[] classes = new Class[type.length];
        for (int i = 0; i < type.length; i++) {
            classes[i] = (Class<?>) type[i].getClass();
        }
        return classes;
    }

}
