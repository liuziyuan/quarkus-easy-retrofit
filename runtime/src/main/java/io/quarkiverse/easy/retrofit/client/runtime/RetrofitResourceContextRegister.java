package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.List;

import io.github.liuziyuan.retrofit.core.*;

public class RetrofitResourceContextRegister {
    public RetrofitResourceContext getContext(RetrofitAnnotationBean retrofitAnnotationBean,
            RetrofitBuilderExtension retrofitBuilderExtension,
            List<RetrofitInterceptorExtension> retrofitInterceptorExtensions) {
        Env env = new QuarkusEnv();
        return new RetrofitResourceContextBuilder(env).build(retrofitAnnotationBean.getBasePackages().toArray(new String[0]),
                retrofitAnnotationBean.getRetrofitBuilderClassSet(),
                retrofitBuilderExtension,
                retrofitInterceptorExtensions);
    }

}
