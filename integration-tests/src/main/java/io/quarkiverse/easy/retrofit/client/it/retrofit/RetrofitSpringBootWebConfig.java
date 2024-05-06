package io.quarkiverse.easy.retrofit.client.it.retrofit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class RetrofitSpringBootWebConfig {

    @Produces
    @ApplicationScoped
    public CustomRetrofitBuilderExtension customRetrofitBuilderExtension() {
        return new CustomRetrofitBuilderExtension();
    }
}
