package de.thws.Adapters.web_in;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class UserControllerIT {

    @Test
    void createAndFetchUser() {
        Long userId = ((Number) given()
                .contentType("application/json")
                .body(Map.of(
                        "username", "userit1",
                        "email", "userit1@test.com",
                        "password", "Abcdef!1"))
                .when()
                .post("/users")
                .then()
                .statusCode(200)
                .body("userId", notNullValue())
                .extract()
                .path("userId")).longValue();

        given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .body("username", equalTo("userit1"));

        given()
                .contentType("application/json")
                .body(Map.of(
                        "email", "userit1-updated@test.com"))
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .body("email", equalTo("userit1-updated@test.com"));
    }
}
