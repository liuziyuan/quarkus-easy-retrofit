package io.quarkiverse.easy.retrofit.client.it.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.quarkiverse.easy.retrofit.extension.test.runtime.Test;
import io.quarkiverse.easy.retrofit.extension.test2.runtime.TestInterceptor2;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "http://localhost:8090")
@Test
@RetrofitInterceptor(handler = TestInterceptor2.class)
public interface BaseApi2 {

    @GET("api/hello")
    Call<ResponseBody> hello();
}
