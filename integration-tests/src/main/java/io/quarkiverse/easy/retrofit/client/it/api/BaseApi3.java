package io.quarkiverse.easy.retrofit.client.it.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

@RetrofitBuilder(baseUrl = "http://localhost:8091")
public interface BaseApi3 {

    @GET("api/hello")
    Call<ResponseBody> hello();
}
