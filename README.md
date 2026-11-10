# Personal Expense Tracker API

A RESTful backend application for managing personal expenses, organizing spending categories, and tracking expense statistics.

Built with Java and Spring Boot, this project demonstrates backend development practices including JWT authentication, role-independent user data isolation, database migrations, pagination, dynamic filtering, and automated testing.

## Features

### Authentication & Authorization

- User registration and login
- Password hashing using BCrypt
- JWT-based authentication
- Stateless session management
- Protected API endpoints
- Authenticated user identification through JWT

### User Management

- User registration
- User-specific data access
- Soft deletion

### Category Management

- Create, retrieve, update, and delete categories
- User-specific categories
- Soft deletion
- Category ownership validation

### Expense Management

- Create, retrieve, update, and delete expenses
- Associate expenses with categories
- User-specific expense records
- Soft deletion
- Expense filtering by:
  - Date range
  - Amount range
  - Category
  - Description
- Pagination and sorting

### Expense Statistics

- Total expense amount
- Number of expenses
- Average expense amount
- Expense breakdown by category
- Date-range-based statistics

### Engineering Practices

- DTO-based request and response handling
- Bean Validation
- Global exception handling
- Transaction management
- JPA EntityGraph to help prevent N+1 queries
- Dynamic queries using Spring Data JPA Specifications
- Database version control with Flyway
- Automated tests
- Docker Compose environment

## Technology Stack

| Technology          | Purpose                          |
| ------------------- | -------------------------------- |
| Java 21             | Programming language             |
| Spring Boot 4.1.2   | Backend framework                |
| Spring Web MVC      | REST API development             |
| Spring Data JPA     | Database persistence             |
| Spring Security     | Authentication and authorization |
| JWT                 | Stateless authentication         |
| BCrypt              | Password hashing                 |
| PostgreSQL          | Relational database              |
| Hibernate           | ORM                              |
| Flyway              | Database migrations              |
| Gradle              | Build automation                 |
| Docker Compose      | Container orchestration          |
| JUnit / Spring Test | Automated testing                |

## Project Structure

```text
src/
├── main/
│   ├── java/com/azure/expensetracker/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   │
│   └── resources/
│       ├── db/
│       │   ├── migration/
│       │   └── mock-data/
│       └── application.properties/
│
└── test/
    └── java/
```

## Database Design

The application uses PostgreSQL with three main entities:

- **User:** Stores user account information.
- **Category:** Stores expense categories and their owners.
- **Expense:** Stores individual expense records, associated with a user and category.

### Entity Relationships

```text
User
 ├── 1:N Categories
 └── 1:N Expenses

Category
 ├── N:1 User
 └── 1:N Expenses

Expense
 ├── N:1 User
 └── N:1 Category
```

### Data Integrity

- Each category belongs to a user.
- Each expense belongs to a user.
- An expense can only reference a category owned by the same user.
- Soft-deleted records are excluded from normal retrieval operations.
- User-specific queries prevent access to other users' data.

## Authentication Flow

1. A user registers with an email, username, and password.
2. The application hashes the password using BCrypt.
3. The user logs in with their credentials.
4. The application verifies the password.
5. A JWT access token is generated.
6. The client includes the token in subsequent protected requests.
7. Spring Security validates the token.
8. The authenticated user ID is extracted from the JWT subject.

Protected endpoints use the following HTTP header:

```http
Authorization: Bearer <access_token>
```

## API Documentation

Interactive API documentation is provided through Swagger UI.

After starting the application, visit:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

The API includes endpoints for:

| Resource           | Operations                                       |
| ------------------ | ------------------------------------------------ |
| Authentication     | Register, Login                                  |
| Categories         | Create, Retrieve, Update, Delete                 |
| Expenses           | Create, Retrieve, Update, Delete                 |
| Expense Filtering  | Filter by date, amount, category and description |
| Expense Statistics | Summary and category breakdown                   |

Refer to Swagger UI for the complete endpoint paths, request schemas, response schemas, and authentication requirements.

## Environment Configuration

The application uses environment variables to keep sensitive configuration outside the source code.

Create a `.env` file in the project root.

Example:

```properties
DB_URL=jdbc:postgresql://localhost:5432/expense_tracker
DB_USERNAME=expense_user
DB_PASSWORD=your_database_password
JWT_SECRET=your_base64_encoded_jwt_secret
```

Do not commit `.env` to version control.

Use `.env.example` as a template for other developers.

## Running the Application

### Prerequisites

- Java 21
- Docker
- Docker Compose
- Git

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd personal-expense-tracker
```

### 2. Configure environment variables

Create your `.env` file using the configuration above.

### 3. Start Docker Compose

```bash
docker compose up -d --build
```

This starts the services defined in your Docker Compose configuration.

### 4. Run the application

If your Compose configuration already starts the Spring Boot application, wait for the application container to become healthy.

Otherwise, run it locally:

```bash
./gradlew bootRun
```

On Windows:

```powershell
.\gradlew.bat bootRun
```

### 5. Access the API

Open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Flyway automatically applies pending database migrations when the application starts.

## Running Tests

Run the automated test suite using Gradle:

Linux / macOS:

```bash
./gradlew test
```

Windows:

```powershell
.\gradlew.bat test
```

## Design Decisions

### JWT Authentication

JWT provides stateless authentication. The authenticated user's ID is derived from the token rather than accepting a user ID from client-controlled request parameters.

### Soft Deletion

Records are marked as deleted instead of being physically removed. This preserves historical data and relationships.

### DTO Pattern

Request and response DTOs separate API contracts from database entities, preventing direct exposure of persistence models.

### Dynamic Filtering

Spring Data JPA Specifications allow optional filters to be combined dynamically without creating a large number of repository methods.

### Transaction Management

Service-layer transactions maintain consistent database operations and allow response mapping while required lazy-loaded relationships are available.

### Database Migrations

Flyway manages schema changes through versioned SQL migration scripts, making database evolution reproducible.

## Future Improvements

Potential extensions include:

- Refresh token mechanism
- Email verification and password recovery
- Monthly and yearly financial reports
- Budget management
- Recurring expenses
- Expense export (CSV / PDF)
- Frontend application
- Production deployment and monitoring

## License

This project is developed for educational and portfolio purposes. Add a license if you intend to distribute or reuse the project publicly.
