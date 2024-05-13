/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.easy.retrofit.client.it;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkiverse.easy.retrofit.client.it.api.BaseApi;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import okhttp3.ResponseBody;
import retrofit2.Call;

@Path("/retrofit-client")
@ApplicationScoped
public class RetrofitClientResource {
    // add some rest methods here
    //
    @Inject
    RetrofitResourceContext retrofitResourceContext;

    @Inject
    BaseApi baseApi;
    //    @Inject
    //    MyTest myTest;
    //
    //    @Inject
    //    RetrofitContext retrofitContext;

    @GET
    public String hello() throws IOException {
        ArcContainer container = Arc.container();
        //        String name = myTest.getName();
        //        System.out.println(name);
        //        String[] basePackages = retrofitResourceContext.getBasePackages();
        //        String[] basePackages = retrofitContext.getBasePackages();
        //        System.out.println(String.join(",", basePackages));
        Call<ResponseBody> hello = baseApi.hello();
        return hello.execute().body().string();
    }
}
