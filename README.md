## Spring Console/Web Demo

This project demonstrates how to run the **same Spring Boot application** in:

- **Normal Web Mode** – starts an embedded web server and exposes REST endpoints.
- **Console/Admin Mode** – runs as a non-web `CommandLineRunner` for administrative tasks (12-factor style admin processes).

The choice of mode is driven entirely by **externalized configuration** (Spring profiles and properties), not by build-time or Gradle-specific wiring. This works the same way in local development and production.

---

## Project structure (key parts)

- **`DemoApplication`**: The single main entry point for the application (`src/main/java/com/example/demo/DemoApplication.java`).
- **`DemoAdminRunner`**: A `CommandLineRunner` that is only active when the `console` profile is enabled. This is where you implement admin/maintenance jobs and dispatch console tasks (`src/main/java/com/example/demo/DemoAdminRunner.java`).
- **`RemoteSyncTask`**: A console task under `console` that performs a remote sync by issuing a GET request to a configured API endpoint (`src/main/java/com/example/demo/console/RemoteSyncTask.java`).
- **`DemoController`**: A simple REST controller, active only in normal (non-console) mode (`src/main/java/com/example/demo/web/DemoController.java`).
- **`application.properties`**: Default configuration for normal web mode.
- **`application-console.properties`**: Configuration that is applied when the `console` profile is active (disables the web server and configures console task settings such as the remote sync URL).

---

## Prerequisites

- JDK 21 (configured via Gradle toolchain).
- Gradle wrapper is included, so you can use `./gradlew` directly.

---

## Build the application

From the project root:

```bash
./gradlew clean bootJar
```

This produces a runnable jar under:

- `build/libs/demo-0.0.1-SNAPSHOT.jar`

---

## Running in normal web mode

In **normal mode**, the application:

- Starts the embedded Spring Boot web server.
- Exposes a simple REST endpoint at `/api/hello`.
- Does **not** run the console/admin runner.

### Run via Gradle (local dev)

```bash
./gradlew bootRun
```

This uses the default configuration from `application.properties` and starts the app in web mode.

### Run the built jar

```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Test the REST endpoint

Once the app is running, call:

```bash
curl http://localhost:8080/api/hello
```

You should see a simple greeting response from the `DemoController`.

---

## Running in console/admin mode

In **console/admin mode**, the application:

- Runs **without** an embedded web server.
- Executes `DemoAdminRunner` (a `CommandLineRunner`).
- Is designed for one-off or scheduled administrative tasks (e.g. scheduled syncs, migrations, etc).

This behavior is enabled using:

- The `console` Spring profile.
- The `application-console.properties` file, which includes:

```properties
spring.application.name=demo-console
spring.main.web-application-type=none

console.remote-sync.url=http://localhost:8080/api/hello
```

### Run console mode from the built jar

The first non-Spring argument is treated as the **console task name** by `DemoAdminRunner`.

To execute the built-in `remote-sync` task:

```bash
java -Dspring.profiles.active=console \
  -jar build/libs/demo-0.0.1-SNAPSHOT.jar \
  remote-sync
```

- `-Dspring.profiles.active=console` enables the `console` profile, which:
  - Activates `DemoAdminRunner` and console components like `RemoteSyncTask`.
  - Applies `application-console.properties` (disabling the web server and configuring the sync URL).
- `remote-sync` (the first positional argument) tells `DemoAdminRunner` to run the `RemoteSyncTask`, which performs a GET request to the URL configured by `console.remote-sync.url`.

In the logs you will see `DemoAdminRunner` starting, logging the arguments, and then `RemoteSyncTask` making the HTTP call and logging the response.

### Notes for production

- You can enable console/admin mode using **any** standard Spring Boot configuration mechanism:
  - Environment variable: `SPRING_PROFILES_ACTIVE=console`
  - System property: `-Dspring.profiles.active=console`
  - Command-line argument: `--spring.profiles.active=console`
- No build changes are required to switch modes; the same jar is used in all environments.

---

## Summary

- **Normal web mode**: default; run with `./gradlew bootRun` or `java -jar ...` and hit `http://localhost:8080/api/hello`.
- **Console/admin mode**: activate via `spring.profiles.active=console` to run `DemoAdminRunner` as a non-web administrative process and invoke console tasks such as `remote-sync` (calling the configured API endpoint).
