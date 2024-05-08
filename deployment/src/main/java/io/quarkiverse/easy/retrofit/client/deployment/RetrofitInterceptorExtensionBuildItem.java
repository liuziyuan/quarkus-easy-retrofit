package io.quarkiverse.easy.retrofit.client.deployment;

import java.util.ArrayList;
import java.util.List;

import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.quarkus.builder.item.SimpleBuildItem;

public final class RetrofitInterceptorExtensionBuildItem extends SimpleBuildItem {

    private final List<String> retrofitInterceptorExtensionClassNames;

    public RetrofitInterceptorExtensionBuildItem(List<String> retrofitInterceptorExtensionClassNames) {
        this.retrofitInterceptorExtensionClassNames = retrofitInterceptorExtensionClassNames;
    }

    public List<Class<? extends RetrofitInterceptorExtension>> getRetrofitInterceptorExtensionClasses()
            throws ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        List<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorExtensionClasses = new ArrayList<>();
        for (String className : retrofitInterceptorExtensionClassNames) {
            Class<?> clazz = cl.loadClass(className);
            retrofitInterceptorExtensionClasses.add((Class<? extends RetrofitInterceptorExtension>) clazz);
        }
        return retrofitInterceptorExtensionClasses;
    }

    public List<String> getRetrofitInterceptorExtensionClassNames() {
        return retrofitInterceptorExtensionClassNames;
    }
}
