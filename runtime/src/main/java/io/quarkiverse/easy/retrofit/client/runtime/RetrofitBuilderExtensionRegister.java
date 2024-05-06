package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;
import io.quarkus.arc.Arc;

public class RetrofitBuilderExtensionRegister {

    public RetrofitBuilderGlobalConfig getGlobalConfig() {
        RetrofitBuilderGlobalConfigProperties properties = new RetrofitBuilderGlobalConfigProperties();
        RetrofitBuilderExtension retrofitBuilderExtension = null;

        return new RetrofitBuilderGlobalConfig(properties, retrofitBuilderExtension);
    }

}
