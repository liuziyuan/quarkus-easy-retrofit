package io.quarkiverse.easy.retrofit.client.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RetrofitClientResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/retrofit-client")
                .then()
                .statusCode(200)
                .body(is("Hello retrofit-client"));
    }
}
