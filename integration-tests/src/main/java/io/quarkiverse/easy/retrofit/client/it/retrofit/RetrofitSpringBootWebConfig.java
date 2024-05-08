package io.quarkiverse.easy.retrofit.client.it.retrofit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class RetrofitSpringBootWebConfig {

    @Produces
    public CustomRetrofitBuilderExtension customRetrofitBuilderExtension() {
        return new CustomRetrofitBuilderExtension();
    }
}
