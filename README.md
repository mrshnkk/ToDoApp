# ToDoApp Backend (Quarkus)

## Project Description

Short description: Task management system (to-do app) as a REST API.

Note: ChatGPT was used for minor wording and debugging support.

## Software

- Java 17+
- Quarkus 3.12.3
- RESTEasy (JAX-RS)
- Hibernate ORM + H2 Database
- Maven

## API Endpoints Overview

### Users
- GET /users/{id} - Fetch user
- POST /users - Create user (JSON: username, email, password)
- PUT /users/{id} - Update user
- DELETE /users/{id} - Delete user

### Tasks
- GET /tasks/{id} - Fetch task
- GET /tasks?assignedUserId=1&status=TODO&page=0&size=10 - Filter tasks
- POST /tasks - Create task
- PUT /tasks/{id} - Update task
- DELETE /tasks/{id} - Delete task
- PUT /tasks/{id}/assign/{userId} - Assign task to user
- DELETE /tasks/{id}/assign - Remove assignment

### Projects
- GET /projects/{id} - Fetch project
- GET /projects?ownerId=1&page=0&size=10 - Filter projects
- POST /projects - Create project
- PUT /projects/{id} - Update project
- DELETE /projects/{id} - Delete project

### Teams
- GET /teams/{id} - Fetch team
- GET /teams?ownerId=1&page=0&size=10 - Filter teams
- POST /teams - Create team
- PUT /teams/{id} - Update team
- DELETE /teams/{id} - Delete team
- GET /teams/{id}/members - List members
- POST /teams/{id}/members/{userId} - Add member
- DELETE /teams/{id}/members/{userId} - Remove member

## Architecture (Hexagonal)

- Domain: `src/main/java/de/thws/Application/Domain`
- Domain Hydrators: `src/main/java/de/thws/Application/Domain/DomainModels/hydrators`
- Use Cases (application services): `src/main/java/de/thws/Application/Services`
- Ports: `src/main/java/de/thws/Application/Ports/in` and `src/main/java/de/thws/Application/Ports/out`
- Adapter (API): `src/main/java/de/thws/Adapters/web_in`
- Adapter (Persistence): `src/main/java/de/thws/Adapters/persistence_out`

## Caching

- GET responses are cached using Quarkus Cache (`@CacheResult`) with a 60s TTL.
- Responses include `Cache-Control: private, max-age=60` for GET requests.

## Hypermedia (HATEOAS)

- Responses include link headers for self/update/delete and collection navigation (`self`, `next`, `prev`).
- Collections also include item-level self links, and relationship operations are exposed via link relations.

## Authentication / Authorization

- Not required and not implemented for this project.


## Build and Run (Local)

```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

The API is available at `http://localhost:8080`.

## Tests

```bash
mvn test
```

```bash
mvn verify
```

Note: `mvn verify` also runs the integration tests (`*IT`).

## Docker

Build the container image:

```bash
docker build -t todoapp-backend .
```

Run the container:

```bash
docker run --rm -p 8080:8080 todoapp-backend
```

Optional: run tests locally before the Docker run:

```bash
mvn verify
```
