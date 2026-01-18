package de.thws.Adapters.web_in;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class ProjectControllerIT {

    @Test
    void createAndQueryProject() {
        Long ownerId = createUser("projit1", "projit1@test.com");

        Long projectId = ((Number) given()
                .contentType("application/json")
                .body(Map.of(
                        "name", "Project IT",
                        "description", "desc",
                        "ownerId", ownerId,
                        "teamId", 7))
                .when()
                .post("/projects")
                .then()
                .statusCode(200)
                .body("projectId", notNullValue())
                .extract()
                .path("projectId")).longValue();

        given()
                .when()
                .get("/projects/" + projectId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Project IT"));

        given()
                .queryParam("ownerId", ownerId)
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("projectId", hasItem(projectId.intValue()));

        given()
                .contentType("application/json")
                .body(Map.of("description", "updated"))
                .when()
                .put("/projects/" + projectId)
                .then()
                .statusCode(200)
                .body("description", equalTo("updated"));
    }

    private static Long createUser(String username, String email) {
        return ((Number) given()
                .contentType("application/json")
                .body(Map.of(
                        "username", username,
                        "email", email,
                        "password", "Abcdef!1"))
                .when()
                .post("/users")
                .then()
                .statusCode(200)
                .extract()
                .path("userId")).longValue();
    }
}
