package io.quarkiverse.easy.retrofit.client.it;

import io.quarkiverse.easy.retrofit.client.runtime.EnableRetrofit;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@EnableRetrofit("io.quarkiverse.easy.retrofit.client.it.api")
@QuarkusMain
public class QuarkusRetrofitClientDemo {
    public static void main(String[] args) {
        Quarkus.run(RetrofitDemoApplication.class, args);
    }

    public static class RetrofitDemoApplication implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            System.out.println("Do startup logic here");
            Quarkus.waitForExit();
            return 0;
        }
    }
}
