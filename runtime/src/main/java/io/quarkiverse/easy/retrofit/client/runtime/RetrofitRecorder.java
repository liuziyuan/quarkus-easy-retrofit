package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
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

    public RuntimeValue<Retrofit> getRetrofitInstance(RetrofitClientBean clientBean, RetrofitResourceContext context) {
        RetrofitBuilderGenerator retrofitBuilderGenerator = new RetrofitBuilderGenerator(clientBean, context,
                new QuarkusCDIBeanManager(Arc.container()));
        final Retrofit.Builder retrofitBuilder = retrofitBuilderGenerator.generate();
        return new RuntimeValue<>(retrofitBuilder.build());
    }

    public RuntimeValue<?> getRetrofitApiInstance(Class<?> interfaceType, RetrofitApiServiceBean retrofitApiServiceBean,
            RuntimeValue<Retrofit> retrofitRuntimeValue) {
        RetrofitApiServiceProxyRegister<?> retrofitApiServiceProxyBuilder = new RetrofitApiServiceProxyRegister<>(interfaceType,
                retrofitApiServiceBean, retrofitRuntimeValue, new QuarkusCDIBeanManager(Arc.container()));

        return new RuntimeValue<>(retrofitApiServiceProxyBuilder.build());
    }

}
