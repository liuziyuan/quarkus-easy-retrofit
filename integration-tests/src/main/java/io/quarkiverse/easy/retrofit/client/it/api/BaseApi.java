package io.quarkiverse.easy.retrofit.client.it.api;

import com.google.common.util.concurrent.ListenableFuture;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.quarkiverse.easy.retrofit.client.it.HelloBean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "${app.url}")
//        , addConverterFactory = {JacksonConvertFactoryBuilder.class}, addCallAdapterFactory = {BodyCallAdapterFactoryBuilder.class,
//        GuavaCallAdapterFactoryBuilder.class}, validateEagerly = false, globalOverwriteRule = OverrideRule.LOCAL_FIRST)
//@RetrofitInterceptor(handler = TestInterceptor.class)
public interface BaseApi {

    @GET("api/hello")
    Call<ResponseBody> hello();

    @GET("backend/v1/hello/abc")
    ListenableFuture<HelloBean> helloBean();

    @GET("backend/v1/hello/abc")
    HelloBean helloBean2();
}
