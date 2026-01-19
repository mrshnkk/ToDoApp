package de.thws.Adapters.web_in;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
class TaskControllerIT {

    @Test
    void createAndQueryTask() {
        Long ownerId = createUser("taskownerit1", "taskownerit1@test.com");
        Long assigneeId = createUser("taskassigneeit1", "taskassigneeit1@test.com");

        Long projectId = ((Number) given()
                .contentType("application/json")
                .body(Map.of(
                        "name", "Task Project IT",
                        "ownerId", ownerId))
                .when()
                .post("/projects")
                .then()
                .statusCode(200)
                .extract()
                .path("projectId")).longValue();

        Long taskId = ((Number) given()
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
                .path("taskId")).longValue();

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
                .body("taskId", hasItem(taskId.intValue()));

        given()
                .when()
                .get("/tasks/assigned/" + assigneeId)
                .then()
                .statusCode(200)
                .body("taskId", hasItem(taskId.intValue()));

        given()
                .queryParam("assignedUserId", assigneeId)
                .queryParam("tags", "urgent")
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("taskId", hasItem(taskId.intValue()));

        given()
                .contentType("application/json")
                .body(Map.of("title", "Task IT Updated"))
                .when()
                .put("/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Task IT Updated"));

        given()
                .when()
                .delete("/tasks/" + taskId + "/assign")
                .then()
                .statusCode(200)
                .body("assignedUser", nullValue());

        given()
                .when()
                .put("/tasks/" + taskId + "/assign/" + assigneeId)
                .then()
                .statusCode(200)
                .body("assignedUser.userId", equalTo(assigneeId.intValue()));
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
