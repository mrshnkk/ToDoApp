package de.thws.Adapters.web_in;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class TaskControllerIT {

    @Test
    void createAndQueryTask() {
        Long ownerId = createUser("taskownerit1", "taskownerit1@test.com");
        Long assigneeId = createUser("taskassigneeit1", "taskassigneeit1@test.com");

        Long projectId = given()
                .contentType("application/json")
                .body(Map.of(
                        "name", "Task Project IT",
                        "ownerId", ownerId))
                .when()
                .post("/projects")
                .then()
                .statusCode(200)
                .extract()
                .path("projectId");

        Long taskId = given()
                .contentType("application/json")
                .body(Map.of(
                        "title", "Task IT",
                        "description", "desc",
                        "projectId", projectId,
                        "assignedUserId", assigneeId,
                        "tags", Set.of("urgent")))
                .when()
                .post("/tasks")
                .then()
                .statusCode(200)
                .body("taskId", notNullValue())
                .extract()
                .path("taskId");

        given()
                .when()
                .get("/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Task IT"));

        given()
                .when()
                .get("/tasks/project/" + projectId)
                .then()
                .statusCode(200)
                .body("taskId", hasItem(taskId));

        given()
                .when()
                .get("/tasks/assigned/" + assigneeId)
                .then()
                .statusCode(200)
                .body("taskId", hasItem(taskId));

        given()
                .contentType("application/json")
                .body(Map.of("title", "Task IT Updated"))
                .when()
                .put("/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Task IT Updated"));
    }

    private static Long createUser(String username, String email) {
        return given()
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
                .path("userId");
    }
}
