package io.quarkiverse.easy.retrofit.client.deployment;

import java.util.Set;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.quarkus.builder.item.SimpleBuildItem;

public final class RetrofitExtensionBuildItem extends SimpleBuildItem {

    private final Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderExtensionClass;

    private final Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorExtensionClass;

    public RetrofitExtensionBuildItem(Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderExtensionClass,
            Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorExtensionClass) {
        this.retrofitBuilderExtensionClass = retrofitBuilderExtensionClass;
        this.retrofitInterceptorExtensionClass = retrofitInterceptorExtensionClass;
    }
}
