package io.quarkiverse.easy.retrofit.client.it.retrofit;

import io.github.liuziyuan.retrofit.adapter.simple.body.BodyCallAdapterFactory;
import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import retrofit2.CallAdapter;

public class BodyCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return BodyCallAdapterFactory.create();
    }
}
