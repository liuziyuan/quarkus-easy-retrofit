package io.quarkiverse.easy.retrofit.client.it.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.quarkiverse.easy.retrofit.extension.test.runtime.TestInterceptor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "${app.url}")
//@RetrofitInterceptor(handler = TestInterceptor.class)
public interface BaseApi {

    @GET("api/hello")
    Call<ResponseBody> hello();
}
