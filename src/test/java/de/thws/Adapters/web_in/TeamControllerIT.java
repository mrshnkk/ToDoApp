package de.thws.Adapters.web_in;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class TeamControllerIT {

    @Test
    void createAndQueryTeam() {
        Long ownerId = createUser("teamit1", "teamit1@test.com");

        Long teamId = ((Number) given()
                .contentType("application/json")
                .body(Map.of(
                        "teamName", "Team IT",
                        "description", "desc",
                        "ownerId", ownerId))
                .when()
                .post("/teams")
                .then()
                .statusCode(200)
                .body("teamId", notNullValue())
                .extract()
                .path("teamId")).longValue();

        given()
                .when()
                .get("/teams/" + teamId)
                .then()
                .statusCode(200)
                .body("teamName", equalTo("Team IT"));

        given()
                .queryParam("ownerId", ownerId)
                .when()
                .get("/teams")
                .then()
                .statusCode(200)
                .body("teamId", hasItem(teamId.intValue()));

        given()
                .contentType("application/json")
                .body(Map.of("teamName", "Team IT Updated"))
                .when()
                .put("/teams/" + teamId)
                .then()
                .statusCode(200)
                .body("teamName", equalTo("Team IT Updated"));
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
