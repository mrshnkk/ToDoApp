# ToDoApp Backend (Quarkus)

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
