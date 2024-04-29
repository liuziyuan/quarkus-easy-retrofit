package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.List;
import java.util.Set;

public class RetrofitAnnotationBean {

    private Set<Class<?>> retrofitBuilderClassSet;

    private List<String> basePackages;

    public RetrofitAnnotationBean(List<String> basePackages, Set<Class<?>> retrofitBuilderClassSet) {
        this.retrofitBuilderClassSet = retrofitBuilderClassSet;
        this.basePackages = basePackages;
    }

    public Set<Class<?>> getRetrofitBuilderClassSet() {
        return retrofitBuilderClassSet;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }
}
