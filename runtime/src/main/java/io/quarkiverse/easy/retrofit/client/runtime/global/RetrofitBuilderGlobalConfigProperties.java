package io.quarkiverse.easy.retrofit.client.runtime.global;

import io.github.liuziyuan.retrofit.core.builder.*;
import io.smallrye.config.ConfigMapping;

/**
 * Spring boot web配置文件中声明的全局配置
 */
@ConfigMapping(prefix = "retrofit.global")
public class RetrofitBuilderGlobalConfigProperties {

    private String enable;

    private String baseUrl;

    private Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryBuilderClazz;

    private Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClazz;

    private Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;

    private Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;

    private Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz;

    private String validateEagerly;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Class<? extends BaseCallAdapterFactoryBuilder>[] getCallAdapterFactoryBuilderClazz() {
        return callAdapterFactoryBuilderClazz;
    }

    public void setCallAdapterFactoryBuilderClazz(
            Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryBuilderClazz) {
        this.callAdapterFactoryBuilderClazz = callAdapterFactoryBuilderClazz;
    }

    public Class<? extends BaseConverterFactoryBuilder>[] getConverterFactoryBuilderClazz() {
        return converterFactoryBuilderClazz;
    }

    public void setConverterFactoryBuilderClazz(Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClazz) {
        this.converterFactoryBuilderClazz = converterFactoryBuilderClazz;
    }

    public Class<? extends BaseOkHttpClientBuilder> getOkHttpClientBuilderClazz() {
        return okHttpClientBuilderClazz;
    }

    public void setOkHttpClientBuilderClazz(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
    }

    public Class<? extends BaseCallBackExecutorBuilder> getCallBackExecutorBuilderClazz() {
        return callBackExecutorBuilderClazz;
    }

    public void setCallBackExecutorBuilderClazz(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
    }

    public Class<? extends BaseCallFactoryBuilder> getCallFactoryBuilderClazz() {
        return callFactoryBuilderClazz;
    }

    public void setCallFactoryBuilderClazz(Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz) {
        this.callFactoryBuilderClazz = callFactoryBuilderClazz;
    }

    public String getValidateEagerly() {
        return validateEagerly;
    }

    public void setValidateEagerly(String validateEagerly) {
        this.validateEagerly = validateEagerly;
    }
}
