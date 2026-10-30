# Personal Expense Tracker API

A RESTful API built with Spring Boot that allows users to manage expenses, organize spending by category, and analyze monthly expenses.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.x
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Gradle
- **Validation:** Spring Boot Validation
- **Testing:** JUnit 5, Mockito
- **API Testing:** Postman

## Features

- **Expense Management:** Create, retrieve, update, and delete expenses.
- **Category Management:** Organize expenses into categories such as food, transportation, and entertainment.
- **Filtering:** Filter expenses by category and date range.
- **Pagination and Sorting:** Retrieve expense records efficiently.
- **Expense Statistics:** Calculate total spending by category and month.
- **Input Validation:** Validate request data and reject invalid expenses.
- **Exception Handling:** Return consistent error responses for invalid requests and failed operations.
- **Unit and Integration Testing:** Verify business logic and API behavior.

## Prerequisites

Make sure the following tools are installed:

- **JDK 21:** Required to compile and run the application.
- **PostgreSQL:** Used to store expense and category data.
- **Gradle:** Used to build the project and manage dependencies.
- **Git:** Used to clone the repository.
- **Postman (optional):** Used to test REST API endpoints.

Verify your installations:

```bash
java -version
gradle -version
psql --version
git --version
```

## Dependencies

The project uses the following main dependencies:

| Dependency             | Purpose                                   |
| ---------------------- | ----------------------------------------- |
| Spring Boot Web        | Build RESTful APIs                        |
| Spring Data JPA        | Database access and repository operations |
| PostgreSQL Driver      | Connect Spring Boot to PostgreSQL         |
| Spring Boot Validation | Validate incoming request data            |
| Spring Boot Test       | Testing support                           |
| JUnit 5                | Unit testing                              |
| Mockito                | Mock dependencies during unit testing     |

Dependencies are managed through Gradle.

## How to Run

### 1. Clone the repository

```bash
git clone <repository-url>
cd expense-tracker
```

Replace `<repository-url>` with the actual repository URL.

### 2. Create the PostgreSQL database

Open PostgreSQL and execute:

```sql
CREATE DATABASE expense_tracker;
```

### 3. Configure the database connection

Update `src/main/resources/application.properties`:

```properties
spring.application.name=expense-tracker

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/expense_tracker}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:your_password}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Replace `your_password` with your PostgreSQL password.

For local development, you can configure environment variables instead:

```bash
DB_URL=jdbc:postgresql://localhost:5432/expense_tracker
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Use your operating system's environment-variable syntax. Do not commit real credentials to Git.

**Note:** `ddl-auto=update` is convenient during initial development. For a more production-oriented setup, consider using Flyway or Liquibase for database schema migrations.

### 4. Run the application

If the project includes the Gradle Wrapper, use it instead of requiring a separately installed Gradle version.

**Linux / macOS:**

```bash
./gradlew bootRun
```

**Windows:**

```powershell
.\gradlew.bat bootRun
```

Alternatively, if Gradle is installed globally:

```bash
gradle bootRun
```

The application should start on:

```text
http://localhost:8080
```

### 5. Test the API

Use Postman or another HTTP client to test the available endpoints.

Example endpoints:

| Method | Endpoint                           | Description                          |
| ------ | ---------------------------------- | ------------------------------------ |
| POST   | `/api/expenses`                    | Create an expense                    |
| GET    | `/api/expenses`                    | Retrieve expenses                    |
| GET    | `/api/expenses/{id}`               | Retrieve an expense by ID            |
| PUT    | `/api/expenses/{id}`               | Update an expense                    |
| DELETE | `/api/expenses/{id}`               | Delete an expense                    |
| GET    | `/api/expenses/statistics/monthly` | Retrieve monthly spending statistics |

These endpoints describe the intended API. They will become available as you implement the corresponding controllers and business logic.

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
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

The package name and structure can be adjusted to match the actual project.

## Testing

Run the test suite with the Gradle Wrapper:

```bash
./gradlew test
```

On Windows:

```powershell
.\gradlew.bat test
```

## Future Improvements

- Add JWT-based authentication and authorization.
- Introduce Flyway for database migrations.
- Add Docker and Docker Compose for consistent development environments.
- Integrate Redis if caching becomes useful.
- Add API documentation with OpenAPI / Swagger.
- Add CI workflows to run tests automatically.

## License

This project is intended for educational and portfolio purposes.
