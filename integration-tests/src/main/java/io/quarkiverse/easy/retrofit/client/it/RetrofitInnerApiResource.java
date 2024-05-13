package io.quarkiverse.easy.retrofit.client.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api")
@ApplicationScoped
public class RetrofitInnerApiResource {

    @GET()
    @Path("/hello")
    public String hello() {
        return "hello BaseApi";
    }
}
