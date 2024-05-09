package io.quarkiverse.easy.retrofit.client.deployment;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.quarkus.builder.item.SimpleBuildItem;

public final class RetrofitBuilderExtensionBuildItem extends SimpleBuildItem {

    private final RetrofitBuilderExtension retrofitBuilderExtension;

    public RetrofitBuilderExtensionBuildItem(RetrofitBuilderExtension retrofitBuilderExtension) {
        this.retrofitBuilderExtension = retrofitBuilderExtension;
    }

    public RetrofitBuilderExtension getRetrofitBuilderExtension() {
        return retrofitBuilderExtension;
    }
}
