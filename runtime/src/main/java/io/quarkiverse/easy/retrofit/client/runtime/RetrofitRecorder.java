package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RetrofitRecorder {
    public RuntimeValue<RetrofitResourceContext> createRetrofitResourceContext(RetrofitAnnotationBean retrofitAnnotationBean,
            RetrofitBuilderGlobalConfigProperties globalConfigProperties,
            Class<? extends RetrofitBuilderExtension> retrofitBuilderExtensionClass,
            List<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorExtensionClasses) {
        CDIBeanManager cdiBeanManager = new QuarkusCDIBeanManager();
        RetrofitBuilderExtension retrofitBuilderExtension = cdiBeanManager.getBean(retrofitBuilderExtensionClass);
        List<RetrofitInterceptorExtension> retrofitInterceptorExtensions = new ArrayList<>();
        for (Class<? extends RetrofitInterceptorExtension> retrofitInterceptorExtensionClass : retrofitInterceptorExtensionClasses) {
            RetrofitInterceptorExtension retrofitInterceptorExtension = cdiBeanManager
                    .getBean(retrofitInterceptorExtensionClass);
            if (retrofitInterceptorExtension != null) {
                retrofitInterceptorExtensions.add(retrofitInterceptorExtension);
            }
        }
        RetrofitBuilderExtensionRegister retrofitBuilderExtensionRegister = new RetrofitBuilderExtensionRegister();
        RetrofitBuilderGlobalConfig globalConfig = retrofitBuilderExtensionRegister.getGlobalConfig(globalConfigProperties,
                retrofitBuilderExtension);
        RetrofitResourceContextRegister retrofitResourceContextRegister = new RetrofitResourceContextRegister();
        RetrofitResourceContext context = retrofitResourceContextRegister.getContext(retrofitAnnotationBean,
                globalConfig, retrofitInterceptorExtensions);
        return new RuntimeValue<>(context);
    }

    public Supplier<?> retrofitResourceContextSupplier(RuntimeValue<RetrofitResourceContext> retrofitResourceContext) {
        return retrofitResourceContext::getValue;
    }
}
