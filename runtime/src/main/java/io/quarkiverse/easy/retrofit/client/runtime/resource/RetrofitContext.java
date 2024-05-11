package io.quarkiverse.easy.retrofit.client.runtime.resource;

import java.util.List;
import java.util.Map;

import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;

public class RetrofitContext {

    private String[] basePackages;

    private Class<?> retrofitBuilderExtensionClazz;
    private List<Class<?>> interceptorExtensionsClasses;

    private Map<String, RetrofitApiServiceBean> retrofitApiServices;

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public Class<?> getRetrofitBuilderExtensionClazz() {
        return retrofitBuilderExtensionClazz;
    }

    public void setRetrofitBuilderExtensionClazz(Class<?> retrofitBuilderExtensionClazz) {
        this.retrofitBuilderExtensionClazz = retrofitBuilderExtensionClazz;
    }

    public List<Class<?>> getInterceptorExtensionsClasses() {
        return interceptorExtensionsClasses;
    }

    public void setInterceptorExtensionsClasses(List<Class<?>> interceptorExtensionsClasses) {
        this.interceptorExtensionsClasses = interceptorExtensionsClasses;
    }

    public Map<String, RetrofitApiServiceBean> getRetrofitApiServices() {
        return retrofitApiServices;
    }

    public void setRetrofitApiServices(Map<String, RetrofitApiServiceBean> retrofitApiServices) {
        this.retrofitApiServices = retrofitApiServices;
    }
}
