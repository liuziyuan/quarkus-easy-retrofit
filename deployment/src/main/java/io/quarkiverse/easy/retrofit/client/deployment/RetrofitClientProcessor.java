package io.quarkiverse.easy.retrofit.client.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.Type;

import io.quarkiverse.easy.retrofit.client.runtime.*;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public final class RetrofitClientProcessor {

    private static final String FEATURE = "retrofit-client";

    static DotName ENABLE_RETROFIT_ANNOTATION = DotName.createSimple(EnableRetrofit.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(STATIC_INIT)
    void scanRetrofitResource(RetrofitRecorder recorder, BeanArchiveIndexBuildItem beanArchiveIndex,
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

    private Class<?>[] getBasePackageClasses(Type[] type) {
        Class<?>[] classes = new Class[type.length];
        for (int i = 0; i < type.length; i++) {
            classes[i] = (Class<?>) type[i].getClass();
        }
        return classes;
    }

}
