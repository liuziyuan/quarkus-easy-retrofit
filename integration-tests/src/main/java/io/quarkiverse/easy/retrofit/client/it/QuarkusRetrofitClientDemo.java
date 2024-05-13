package io.quarkiverse.easy.retrofit.client.it;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkiverse.easy.retrofit.client.runtime.EnableRetrofit;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@EnableRetrofit(value = "io.quarkiverse.easy.retrofit.client.it.api", extensionPackages = "io.quarkiverse.easy.retrofit.client.it.extension")
@QuarkusMain
public class QuarkusRetrofitClientDemo {
    public static void main(String[] args) {
        Quarkus.run(RetrofitDemoApplication.class, args);
    }

    public static class RetrofitDemoApplication implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            System.out.println("Do startup logic here");
            ArcContainer container = Arc.container();
            InstanceHandle<RetrofitResourceContext> retrofitResourceContextInstanceHandle = container
                    .instance(RetrofitResourceContext.class);
            Quarkus.waitForExit();
            return 0;
        }
    }
}
