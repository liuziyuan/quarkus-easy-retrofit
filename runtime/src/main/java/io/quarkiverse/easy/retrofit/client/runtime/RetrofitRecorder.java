package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import retrofit2.Retrofit;

@Recorder
public class RetrofitRecorder {
    public RuntimeValue<RetrofitResourceContext> getRetrofitResourceContextInstance(RetrofitResourceContext contextInstance) {
        return new RuntimeValue<>(contextInstance);
    }

    public RuntimeValue<?> getRetrofitInstance(RetrofitClientBean clientBean, RetrofitResourceContext context) {
        RetrofitBuilderGenerator retrofitBuilderGenerator = new RetrofitBuilderGenerator(clientBean, context,
                new QuarkusCDIBeanManager(Arc.container()));
        final Retrofit.Builder retrofitBuilder = retrofitBuilderGenerator.generate();
        return new RuntimeValue<>(retrofitBuilder.build());
    }

}
