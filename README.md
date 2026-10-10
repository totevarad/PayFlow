# PayFlow API

PayFlow is a simplified version of a UPI-style backend system like PhonePe or Google Pay. It provides a REST API to register users, manage wallet balances, and record money transfers between them.

## Setup & Running the App

### Prerequisites
- Java 17
- Maven

### How to Run
1. Navigate to the project root directory.
2. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The application will start on `http://localhost:8080`.
4. You can access the H2 Database console at `http://localhost:8080/h2-console`.

## Project Structure (Layers)

The project is organized into four main layers:

1. **Entity Layer (`com.payflow.entity`)**: Contains JPA entity classes (`User` and `Transaction`) that map directly to database tables.
2. **Repository Layer (`com.payflow.repository`)**: Contains Spring Data JPA interfaces for database operations, abstracting away SQL queries.
3. **Service Layer (`com.payflow.service`)**: Contains business logic (`UserService` and `TransactionService`). This is where we process transactions and handle core rules.
4. **Controller Layer (`com.payflow.controller`)**: Contains REST API endpoints (`UserController` and `TransactionController`) that handle HTTP requests and responses.

## Spring Boot Features in PayFlow

1. **Embedded Server**: PayFlow runs on an embedded Apache Tomcat server (included via `spring-boot-starter-web`). You don't need to install or configure an external web server; just run the `main` method and the app listens on port 8080.
2. **Auto-Configuration**: Spring Boot automatically configures the H2 database and JPA (`spring-boot-starter-data-jpa`) just by having them on the classpath. We didn't have to write verbose `DataSource` or `EntityManager` configuration files.
3. **Production-Ready Defaults**: By default, Spring Boot provides sensible defaults like JSON serialization/deserialization out of the box (via Jackson), robust error handling, and default connection pooling (HikariCP), making the API ready to be consumed immediately.
