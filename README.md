# Personal Expense Tracker API

A RESTful API built with Java and Spring Boot that allows users to manage expenses, organize spending by category, and analyze monthly expenses.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.x
- **Database:** PostgreSQL 17
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Gradle
- **Database Environment:** Docker Compose
- **Validation:** Jakarta Bean Validation
- **Boilerplate Reduction:** Project Lombok
- **Testing:** JUnit 5, Mockito, Spring Boot Test

## Features

- **Expense Management:** Create, retrieve, update, and delete expenses.
- **Category Management:** Organize expenses into categories such as food, transportation, and entertainment.
- **Filtering:** Filter expenses by category and date range.
- **Pagination and Sorting:** Retrieve expense records efficiently.
- **Expense Statistics:** Calculate total spending by category and month.
- **Input Validation:** Validate incoming requests and reject invalid expense data.
- **Exception Handling:** Return consistent error responses for failed operations.
- **Automated Testing:** Test business logic, validation, and API behavior.

## Prerequisites

Install the following tools before running the application:

- **JDK 21:** Required to compile and run the application.
- **Docker Desktop** or Docker Engine with the Docker Compose plugin: Runs PostgreSQL in a container.
- **Git:** Used to clone the repository.

Gradle does not need to be installed globally because the project uses the Gradle Wrapper.

Verify your installations:

```bash
java -version
git --version
docker --version
docker compose version
```

## Dependencies

The project uses the following main dependencies:

| Dependency                            | Purpose                                   |
| ------------------------------------- | ----------------------------------------- |
| `spring-boot-starter-webmvc`          | Build REST APIs using Spring MVC          |
| `spring-boot-starter-data-jpa`        | Database access through JPA and Hibernate |
| `spring-boot-starter-validation`      | Validate request data                     |
| `postgresql`                          | Connect to PostgreSQL                     |
| `lombok`                              | Reduce repetitive Java code               |
| `spring-boot-starter-test`            | General testing support                   |
| `spring-boot-starter-data-jpa-test`   | Test JPA-related functionality            |
| `spring-boot-starter-validation-test` | Test validation behavior                  |
| `spring-boot-starter-webmvc-test`     | Test Spring MVC controllers               |
| `junit-platform-launcher`             | Support JUnit Platform test execution     |

Dependencies and compatible versions are managed through the Spring Boot Gradle plugin and dependency management.

## Project Structure

```text
expense-tracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/expensetracker/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       └── ExpenseTrackerApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── compose.yaml
├── .env.example
├── .gitignore
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

The package structure is a proposed layout and may change as the application develops.

## Configuration

### 1. Create the environment file

Create a `.env` file in the project root.

Use the following configuration:

```dotenv
DB_NAME=expense_tracker
DB_USERNAME=expense_user
DB_PASSWORD=change_me_for_local_development
POSTGRES_PORT=5432
DB_URL=jdbc:postgresql://localhost:5432/expense_tracker
```

These values are for local development. Change the password to your preferred development credential.

Create a `.env.example` file containing the same variable names and example values. Commit `.env.example` to Git, but never commit your actual `.env` file or production credentials.

### 2. Configure PostgreSQL with Docker Compose

The `compose.yaml` file defines the PostgreSQL service.

```yaml
services:
  postgres:
    image: postgres:17
    container_name: expense-tracker-db
    restart: unless-stopped

    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}

    ports:
      - "${POSTGRES_PORT:-5432}:5432"

    volumes:
      - expense_tracker_data:/var/lib/postgresql/data

    healthcheck:
      test:
        - CMD-SHELL
        - pg_isready -U "$$POSTGRES_USER" -d "$$POSTGRES_DB"
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  expense_tracker_data:
```

Docker Compose reads the `.env` file to substitute the configuration variables.

The named volume preserves database data when the container stops or is recreated.

### 3. Configure Spring Boot

Update `src/main/resources/application.properties`:

```properties
spring.application.name=expense-tracker

spring.config.import=optional:file:.env[.properties]

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/expense_tracker}
spring.datasource.username=${DB_USERNAME:expense_user}
spring.datasource.password=${DB_PASSWORD:change_me_for_local_development}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Spring Boot imports the local `.env` file as properties. The fallback values allow the application to start with default local configuration when the corresponding properties are not supplied elsewhere.

**Note:** `spring.jpa.hibernate.ddl-auto=update` is intended for development. Consider using Flyway or Liquibase for schema migrations as the project matures.

## How to Run

### 1. Clone the repository

```bash
git clone <repository-url>
cd expense-tracker
```

Replace `<repository-url>` with the actual repository URL.

### 2. Configure the environment

Create `.env` in the project root using the configuration described above.

Make sure `.env` contains the same database name, username, and password used by Docker Compose and Spring Boot.

### 3. Start PostgreSQL

Run:

```bash
docker compose up -d
```

Check the container status:

```bash
docker compose ps
```

View database logs if necessary:

```bash
docker compose logs postgres
```

Wait until PostgreSQL is healthy before starting the application.

### 4. Start Spring Boot

On Windows:

```powershell
.\gradlew.bat bootRun
```

On Linux or macOS:

```bash
./gradlew bootRun
```

The application should start at:

```text
http://localhost:8080
```

### 5. Test the application

Use Postman or another HTTP client to test the implemented API endpoints.

Example endpoints planned for the application:

| Method | Endpoint                           | Description                          |
| ------ | ---------------------------------- | ------------------------------------ |
| POST   | `/api/expenses`                    | Create an expense                    |
| GET    | `/api/expenses`                    | Retrieve expenses                    |
| GET    | `/api/expenses/{id}`               | Retrieve an expense by ID            |
| PUT    | `/api/expenses/{id}`               | Update an expense                    |
| DELETE | `/api/expenses/{id}`               | Delete an expense                    |
| GET    | `/api/expenses/statistics/monthly` | Retrieve monthly spending statistics |

These endpoints will become available as their corresponding controllers and business logic are implemented.

## Testing

Run the test suite using the Gradle Wrapper.

**Windows:**

```powershell
.\gradlew.bat test
```

**Linux / macOS:**

```bash
./gradlew test
```

## Stopping the Environment

Stop PostgreSQL without removing the container:

```bash
docker compose stop
```

Start it again later:

```bash
docker compose start
```

Alternatively, remove the container while retaining the database volume:

```bash
docker compose down
```

Avoid `docker compose down -v` unless you intentionally want to delete the database volume and its data.

## Future Improvements

- Add JWT-based authentication and authorization.
- Introduce Flyway for database migrations.
- Containerize the Spring Boot application.
- Add Docker Compose profiles for development and testing.
- Add OpenAPI / Swagger documentation.
- Implement CI workflows for automated testing.
- Introduce Redis if caching becomes useful.

## License

This project is intended for educational and portfolio purposes.
