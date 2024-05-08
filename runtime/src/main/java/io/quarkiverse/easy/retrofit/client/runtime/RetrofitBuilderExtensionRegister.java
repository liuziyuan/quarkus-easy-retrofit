package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfig;
import io.quarkiverse.easy.retrofit.client.runtime.global.RetrofitBuilderGlobalConfigProperties;

public class RetrofitBuilderExtensionRegister {

    public RetrofitBuilderGlobalConfig getGlobalConfig(RetrofitBuilderGlobalConfigProperties properties,
            RetrofitBuilderExtension retrofitBuilderExtension) {
        return new RetrofitBuilderGlobalConfig(properties, retrofitBuilderExtension);
    }

}
