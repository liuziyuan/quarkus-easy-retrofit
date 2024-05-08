package io.quarkiverse.easy.retrofit.client.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

public final class RetrofitBuilderExtensionBuildItem extends SimpleBuildItem {

    //    private final RetrofitBuilderExtension customRetrofitBuilderExtension;
    private final String retrofitBuilderExtensionClassName;

    public RetrofitBuilderExtensionBuildItem(
            String retrofitBuilderExtensionClassName) {
        //        this.customRetrofitBuilderExtension = customRetrofitBuilderExtension;
        this.retrofitBuilderExtensionClassName = retrofitBuilderExtensionClassName;
    }

    public Class<?> getRetrofitBuilderExtensionClass() throws ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.loadClass(retrofitBuilderExtensionClassName);
    }

    //    public RetrofitBuilderExtension getCustomRetrofitBuilderExtension() {
    //        return customRetrofitBuilderExtension;
    //    }

    public String getRetrofitBuilderExtensionClassName() {
        return retrofitBuilderExtensionClassName;
    }
}
