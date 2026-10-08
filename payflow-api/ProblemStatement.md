# PayFlow - Backend of a UPI-style Payment App

## 1. Brief

### 1.1. The Scenario
Every time you tap Pay on PhonePe or Google Pay, a backend server receives that request, validates the sender, records the transaction, and updates balances, all in under a second.

**PayFlow** is a simplified version of that system. You are building the REST API that powers it: registering users, giving them a wallet balance, and recording money transfers between them.

There is no UI. No frontend. Just a clean, database-backed API that a frontend team could plug into tomorrow—which is exactly how real fintech backends are built and handed off.

By the end of this assignment, the following will work entirely through HTTP calls from your terminal:
- **Register a user**: `POST` a new user with a name, UPI ID, and opening balance.
- **Look up a user**: `GET` a user by their ID, or search by their UPI ID.
- **List all users**: `GET` every registered user in the system.
- **Send money**: `POST` a transaction from one user to another. The amount and sender/receiver get persisted.

---

## 2. Entities

PayFlow involves two main entities for this assignment: the **User** and the **Transaction**. Keep it simple—relationships (like linking transactions to users via foreign keys) are a topic for a future session.

### 2.1. The User Entity
The User entity should contain the basic information of a user.
*(Notice that camelCase Java field names like `upiId`, `phoneNumber` automatically become snake_case column names `upi_id`, `phone_number` in the database. This is the JPA naming convention.)*

**Fields:**
- `userId` (Long): Primary key, auto-generated. Never in the POST body. (DB Column: `user_id`)
- `name` (String): Full name, e.g. "Priya Sharma". (DB Column: `name`)
- `upiId` (String): e.g. "priya@okaxis" — must be unique. (DB Column: `upi_id`)
- `balance` (Double): Opening wallet balance in INR, e.g. 5000.0. (DB Column: `balance`)
- `phoneNumber` (String): 10-digit mobile number. (DB Column: `phone_number`)

### 2.2. The Transaction Entity
A Transaction records a single money transfer. Build it as a plain `@Entity`. You do not need to model the relationship between Transaction and User with foreign keys yet. Simply store the sender and receiver UPI IDs as plain strings for now.

**Fields:**
- `transactionId` (Long): Primary key, auto-generated. (DB Column: `transaction_id`)
- `senderUpiId` (String): UPI ID of the person sending money. (DB Column: `sender_upi_id`)
- `receiverUpiId` (String): UPI ID of the person receiving money. (DB Column: `receiver_upi_id`)
- `amount` (Double): Amount transferred in INR. (DB Column: `amount`)
- `note` (String): Optional note, e.g. "dinner split". (DB Column: `note`)

---

## 3. Tasks

### Task 1 — Project Setup (10 marks)
1. Create a Spring Boot project via **Spring Initializr** with:
   - Spring Web
   - Spring Data JPA
   - H2 Database
2. Organise into four packages: `entity`, `repository`, `service`, `controller`.
3. Write a `README.md` covering:
   - How to run the app.
   - What each layer does.
   - How Spring Boot's three features (embedded server, auto-configuration, production-ready defaults) appear in this PayFlow project specifically.

### Task 2 — Entities & Database (15 marks)
1. Create both the `User` and `Transaction` entity classes with the required fields.
2. Annotate each with `@Entity`, `@Id`, `@GeneratedValue`. No hand-written SQL — DataJPA creates both tables automatically.
3. Add `spring.jpa.show-sql=true` to `application.properties`. On first startup, paste the two `CREATE TABLE (...)` statements from the console into your write-up.
4. Open `/h2-console` and screenshot both tables (`SELECT * FROM USER` and `SELECT * FROM TRANSACTION`) showing correct columns before any data is inserted.

### Task 3 — Repository Layer (10 marks)
1. Create `UserRepository` extending `JpaRepository<User, Long>` and `TransactionRepository` extending `JpaRepository<Transaction, Long>`.
2. In `UserRepository`, add the derived query method `findByUpiId(String upiId)`. This is how your app will look up a user before sending money.
3. In your README, paste the SQL JPA generates for `findByUpiId` and explain: 
   - (a) How JPA derives it from the method name.
   - (b) What the `?` placeholder means.

### Task 4 — Service Layer (15 marks)
1. Create `UserService` (`@Service`) with methods: 
   - `registerUser(User user)`
   - `getAllUsers()`
   - `getUserById(Long id)`
   - `findByUpiId(String upiId)`
2. Create `TransactionService` (`@Service`) with method: 
   - `sendMoney(Transaction transaction)`. *(For now, this just saves the transaction record — balance deduction logic is not required.)*
3. In both services, inject the respective repository using `@Autowired`. Add a comment in the code explaining what Spring is doing at startup to make this work.

### Task 5 — Controller & REST Endpoints (30 marks)
1. Create `UserController` (`@RestController`, base path `/users`) and `TransactionController` (`@RestController`, base path `/transactions`).
2. Implement the following endpoints:
   - `POST /users`: Register a new user. Accept JSON body with `@RequestBody`. Return the saved User object (reflective response — caller receives the record back with the generated userId).
   - `GET /users`: Return all registered users as a JSON array.
   - `GET /users/{id}`: Return a single user by their userId using `@PathVariable`.
   - `POST /transactions`: Record a money transfer. Accept `senderUpiId`, `receiverUpiId`, `amount`, and `note` in the JSON body. Return the saved Transaction.
3. Test all four endpoints using `curl` from your terminal. Include the `curl` commands and output for each. 
4. After registering two users and sending money between them, screenshot `SELECT * FROM USER` and `SELECT * FROM TRANSACTION` in the H2 console.
5. Demonstrate `@RequestBody`: call `POST /users` once with `@RequestBody` and once without. Show the difference in what the `User` object looks like inside the controller (debugger or println). Explain in one paragraph why the fields are null without it.

### Task 6 — Custom Query (10 marks)
1. Wire `findByUpiId` into the service and controller, add a `GET /users/upi/{upiId}` endpoint that returns the user matching that UPI ID.
2. Add a second method to `UserRepository` using the `@Query` annotation with a JPQL query (not native SQL) — for example, find all users whose balance is above a given amount.
3. In your README, compare the three approaches to custom queries: derived method names, `@Query` (JPQL), and native SQL, and explain why native queries are the least preferred.

---

## 4. Conceptual Write-Up (10 marks)

Answer all six questions in your own words, 3–5 sentences each. Every question maps to something covered in class.

1. **Request lifecycle**: Trace what happens from the moment `curl` sends `POST /users` to the moment your `createUser` method runs. Name the Dispatcher Servlet and Handler Adapter in your answer.
2. **Serialisation**: When you POST a JSON payload like `{"name":"Priya","upiId":"priya@okaxis"}`, what converts it into a Java `User` object? What happens if the JSON key is `"upi_id"` instead of `"upiId"`?
3. **Spring Boot features**: Name the three Spring Boot features. For each one, point to a specific thing in your PayFlow project where that feature is doing work for you.
4. **Spring vs. Spring Boot**: If you had used plain Spring instead of Spring Boot for PayFlow, what would you have had to set up manually? What does Spring Boot take care of automatically?
5. **Stateless REST**: Your `POST /transactions` endpoint does not remember anything about the previous request. What does stateless mean, and why does it matter if PayFlow eventually runs on three servers behind a load balancer?
6. **Persistence**: You stored transactions in the H2 database, not in a Java List. What would have happened to all the transaction records if you had used a List and then restarted the server? Why is this unacceptable for a payments app?

---

## 5. Submission Guidelines
1. Push the complete Spring Boot project to a public GitHub repo, name it `payflow-api` or similar.
2. Place your write-up (answers + screenshots) as a PDF or Word doc in the repo root.
3. Make sure the app starts cleanly from a fresh run before submitting.
