# ToDoApp Backend (Quarkus)

## Projektbeschreibung

Kurzbeschreibung: Task Management System (To-Do App) als REST-API.

## Software

- Java 17+
- Quarkus 3.12.3
- RESTEasy (JAX-RS)
- Hibernate ORM + H2 Database
- Maven

## API Endpoints Uebersicht

### Users
- GET /users/{id} - User abrufen
- POST /users - User erstellen (JSON: username, email, password)
- PUT /users/{id} - User aktualisieren
- DELETE /users/{id} - User loeschen

### Tasks
- GET /tasks/{id} - Task abrufen
- GET /tasks?assignedUserId=1&status=TODO&page=0&size=10 - Tasks filtern
- POST /tasks - Task erstellen
- PUT /tasks/{id} - Task aktualisieren
- DELETE /tasks/{id} - Task loeschen
- PUT /tasks/{id}/assign/{userId} - Task einem User zuweisen
- DELETE /tasks/{id}/assign - Zuweisung entfernen

### Projects
- GET /projects/{id} - Projekt abrufen
- GET /projects?ownerId=1&page=0&size=10 - Projekte filtern
- POST /projects - Projekt erstellen
- PUT /projects/{id} - Projekt aktualisieren
- DELETE /projects/{id} - Projekt loeschen

### Teams
- GET /teams/{id} - Team abrufen
- GET /teams?ownerId=1&page=0&size=10 - Teams filtern
- POST /teams - Team erstellen
- PUT /teams/{id} - Team aktualisieren
- DELETE /teams/{id} - Team loeschen
- GET /teams/{id}/members - Mitglieder anzeigen
- POST /teams/{id}/members/{userId} - Mitglied hinzufuegen
- DELETE /teams/{id}/members/{userId} - Mitglied entfernen

## Architektur (Hexagonal)

- Domain: `src/main/java/de/thws/Application/Domain` und `src/main/java/de/thws/Application/Services`
- Ports: `src/main/java/de/thws/Application/Ports/in` und `src/main/java/de/thws/Application/Ports/out`
- Adapter (API): `src/main/java/de/thws/Adapters/web_in`
- Adapter (Persistence): `src/main/java/de/thws/Adapters/persistence_out`

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

## Docker

Build the container image:

```bash
docker build -t todoapp-backend .
```

Run the container:

```bash
docker run --rm -p 8080:8080 todoapp-backend
```
