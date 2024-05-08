package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RetrofitRecorder {

    public RetrofitBuilderExtension createRetrofitBuilderExtension(BeanContainer beanContainer,
            String retrofitBuilderExtensionClass) {
        InstanceHandle<Object> instance = Arc.container().instance(retrofitBuilderExtensionClass);
        Object o = instance.get();
        return (RetrofitBuilderExtension) o;
    }

    public RetrofitBuilderExtension getRetrofitBuilderExtension(String className) {
        Object o = Arc.container().instance(className).get();
        return (RetrofitBuilderExtension) o;
    }
}
