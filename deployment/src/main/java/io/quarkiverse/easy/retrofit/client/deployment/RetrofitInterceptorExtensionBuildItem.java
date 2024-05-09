package io.quarkiverse.easy.retrofit.client.deployment;

import java.util.List;

import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.quarkus.builder.item.SimpleBuildItem;

public final class RetrofitInterceptorExtensionBuildItem extends SimpleBuildItem {

    private final List<RetrofitInterceptorExtension> retrofitInterceptorExtensions;

    public RetrofitInterceptorExtensionBuildItem(List<RetrofitInterceptorExtension> retrofitInterceptorExtensions) {
        this.retrofitInterceptorExtensions = retrofitInterceptorExtensions;
    }

    public List<RetrofitInterceptorExtension> getRetrofitInterceptorExtensions() {
        return retrofitInterceptorExtensions;
    }
}
