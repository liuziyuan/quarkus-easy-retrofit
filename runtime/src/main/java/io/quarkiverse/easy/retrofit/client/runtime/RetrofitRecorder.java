package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.List;
import java.util.function.Supplier;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RetrofitRecorder {
    public RuntimeValue<RetrofitResourceContext> createRetrofitResourceContext(RetrofitAnnotationBean retrofitAnnotationBean,
            RetrofitBuilderGlobalConfigProperties globalConfigProperties,
            Class<? extends RetrofitBuilderExtension> retrofitBuilderExtensionClass,
            List<Class<?>> retrofitInterceptorExtensionClasses) {
        RetrofitBuilderExtension retrofitBuilderExtension = Arc.container().instance(retrofitBuilderExtensionClass).get();
        RetrofitBuilderExtensionRegister retrofitBuilderExtensionRegister = new RetrofitBuilderExtensionRegister();
        RetrofitBuilderGlobalConfig globalConfig = retrofitBuilderExtensionRegister.getGlobalConfig(globalConfigProperties,
                retrofitBuilderExtension);
        RetrofitResourceContextRegister retrofitResourceContextRegister = new RetrofitResourceContextRegister();
        RetrofitResourceContext context = retrofitResourceContextRegister.getContext(retrofitAnnotationBean,
                globalConfig, null);
        return new RuntimeValue<>(context);
    }

    public Supplier<?> retrofitResourceContextSupplier(RuntimeValue<RetrofitResourceContext> retrofitResourceContext) {
        return retrofitResourceContext::getValue;
    }
}
