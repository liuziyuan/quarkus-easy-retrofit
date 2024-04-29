package io.quarkiverse.easy.retrofit.client.deployment;

import io.quarkiverse.easy.retrofit.client.runtime.RetrofitAnnotationBean;
import io.quarkus.builder.item.SimpleBuildItem;

public final class EnableRetrofitBuildItem extends SimpleBuildItem {

    private final RetrofitAnnotationBean retrofitAnnotationBean;

    public EnableRetrofitBuildItem(RetrofitAnnotationBean retrofitAnnotationBean) {
        this.retrofitAnnotationBean = retrofitAnnotationBean;
    }

    public String getBeanName() {
        return retrofitAnnotationBean.getClass().getName();
    }

    public Class<?> getBeanClass() {
        return retrofitAnnotationBean.getClass();
    }

    public RetrofitAnnotationBean getRetrofitAnnotationBean() {
        return retrofitAnnotationBean;
    }
}
