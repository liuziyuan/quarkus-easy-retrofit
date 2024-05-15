package io.quarkiverse.easy.retrofit.client.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.io.IOException;
import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import io.github.liuziyuan.retrofit.core.*;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.quarkiverse.easy.retrofit.client.runtime.*;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.deployment.*;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.runtime.RuntimeValue;
import retrofit2.Retrofit;

public final class RetrofitClientProcessor {

    private static final Logger LOG = Logger.getLogger(RetrofitClientProcessor.class);
    private static final String FEATURE = "retrofit-client";

    static DotName ENABLE_RETROFIT_ANNOTATION = DotName.createSimple(EnableRetrofit.class.getName());
    static final String RETROFIT_EXTENSION_PROPERTIES = "META-INF/retrofit-extension.properties";
    private static final String RETROFIT_EXTENSION_CLASS_NAME = "retrofit.extension.name";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void reflectiveClasses(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        reflectiveClass.produce(
                ReflectiveClassBuildItem.builder(RetrofitResourceContext.class).methods(true).fields(true).build());
    }

    @BuildStep
    @Record(STATIC_INIT)
    void registerRetrofitResource(RetrofitRecorder recorder,
            BeanArchiveIndexBuildItem beanArchiveIndex,
            RetrofitBuilderGlobalConfigProperties globalConfigProperties,
            BuildProducer<RetrofitResourceContextBuildItem> producer,
            BuildProducer<SyntheticBeanBuildItem> syntheticBeanBuildItemBuildProducer) throws IOException {
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
            RetrofitAnnotationBeanRegister retrofitAnnotationBeanRegister = new RetrofitAnnotationBeanRegister();
            RetrofitAnnotationBean retrofitAnnotationBean = retrofitAnnotationBeanRegister.build(enableRetrofitBean);

            RetrofitResourceContextRegister register = new RetrofitResourceContextRegister();
            RetrofitResourceContext retrofitResourceContextInstance = register
                    .getRetrofitResourceContextInstance(retrofitAnnotationBean, globalConfigProperties);

            SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                    .configure(RetrofitResourceContext.class)
                    .scope(Singleton.class)
                    .unremovable()
                    .runtimeValue(recorder.getRetrofitResourceContextInstance(retrofitAnnotationBean, globalConfigProperties));
            syntheticBeanBuildItemBuildProducer.produce(configurator.done());

            producer.produce(new RetrofitResourceContextBuildItem(retrofitResourceContextInstance));
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

            List<RetrofitClientBean> retrofitClientBeanList = context.getRetrofitClients();
            for (RetrofitClientBean clientBean : retrofitClientBeanList) {
                RuntimeValue<Retrofit> retrofitInstance = recorder.getRetrofitInstance(clientBean.getRetrofitInstanceName());
                for (RetrofitApiServiceBean serviceBean : clientBean.getRetrofitApiServiceBeans()) {
                    SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
                            .configure(serviceBean.getSelfClazz())
                            .setRuntimeInit()
                            .scope(ApplicationScoped.class)
                            .unremovable()
                            .runtimeValue(
                                    recorder.getRetrofitApiInstance(serviceBean.getSelfClazz(), retrofitInstance))
                            .addQualifier().annotation(Named.class).addValue("value", serviceBean.getSelfClazz().getName())
                            .done();
                    syntheticBeanBuildItemBuildProducer.produce(configurator.done());
                }
            }

            RetrofitLogoVersion retrofitLogoVersion = new RetrofitLogoVersion(context);
            retrofitLogoVersion.print();

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
