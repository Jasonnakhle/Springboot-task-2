# Library Management System

A Spring Boot REST API for managing books, authors, borrowers, and borrowing transactions. The project uses DTOs for API data, validation annotations for input checks, service-layer logging, and an in-memory H2 database for local development.

## Assignment coverage

The project implements CRUD endpoints for books, authors, and borrowers, and create/read/return/delete operations for borrowing transactions. It also checks book availability before borrowing and updates availability when borrowing or returning.

The assignment additionally asks for searching books by title, category, or author, including category filtering. The current source has an author-to-books endpoint, but no title or category search/filter endpoint. Add those mappings before describing that requirement as complete.

## Technologies

- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA / Hibernate
- H2 Database (configured datasource)
- Jakarta Bean Validation
- Maven Wrapper

The project also declares the PostgreSQL JDBC driver, but `application.properties` currently configures H2.

## Requirements

- JDK 21
- No separate database server is needed for the configured H2 in-memory database.

## Run locally

From the project root:

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

The application uses Spring Boot's default port, `8080`, unless overridden. Base URL:

```text
http://localhost:8080
```

## H2 configuration

The current `src/main/resources/application.properties` configures:

```properties
spring.datasource.url=jdbc:h2:mem:librarydb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

This is an in-memory database. Data is temporary and is lost when the application stops. Hibernate updates the schema at startup. The H2 console is enabled at Spring Boot's default path:

```text
http://localhost:8080/h2-console
```

Use JDBC URL `jdbc:h2:mem:librarydb`, username `sa`, and a blank password in the console.

## API conventions

- All routes below are rooted at `http://localhost:8080`.
- JSON request bodies use `Content-Type: application/json`.
- Resource IDs are numeric path parameters.
- Create/update methods return DTO JSON. Delete methods return no response body.
- The code does not define a custom exception handler or a documented error-body schema. Error status and payload details should therefore be confirmed from the running application rather than assumed here.

## Books API

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/books` | Create a book |
| `GET` | `/api/books` | List all books |
| `GET` | `/api/books/{id}` | Get one book |
| `PUT` | `/api/books/{id}` | Update a book |
| `DELETE` | `/api/books/{id}` | Delete a book |

There is no implemented title/category search route in the current controller. Category is an enum with values `FICTION`, `NON_FICTION`, `SCIENCE`, `HISTORY`, `TECHNOLOGY`, and `OTHER`.

Book DTO fields:

| Field | Type | Notes |
|---|---|---|
| `id` | number | Returned by the API; ignored as a path-independent identifier for create/update |
| `title` | string | Required, non-blank |
| `isbn` | string | Required, non-blank |
| `category` | string | Required, non-blank; must match an enum value above |
| `available` | boolean | Included in DTO responses; availability is managed by borrowing/return logic |
| `authorId` | number | Required; references an existing author |

Example create request:

```http
POST /api/books
Content-Type: application/json

{
  "title": "The Left Hand of Darkness",
  "isbn": "9780441478125",
  "category": "FICTION",
  "authorId": 1
}
```

New books start as available in the entity. The create controller does not use an incoming `available` value. The update controller also does not copy `available` from the DTO into the entity; the service then reads that missing value, which can cause an error during `PUT`. See [Known behavior and gaps](#known-behavior-and-gaps).

## Authors API

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/authors` | Create an author |
| `GET` | `/api/authors` | List all authors |
| `GET` | `/api/authors/{id}` | Get one author |
| `PUT` | `/api/authors/{id}` | Update an author |
| `DELETE` | `/api/authors/{id}` | Delete an author |
| `GET` | `/api/authors/{id}/books` | List books for an author |

Author DTO fields are `id` (number), `name` (required non-blank string), and `biography` (string, optional).

Example create request:

```http
POST /api/authors
Content-Type: application/json

{
  "name": "Ursula K. Le Guin",
  "biography": "American author of speculative fiction."
}
```

## Borrowers API

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/borrowers` | Create a borrower |
| `GET` | `/api/borrowers` | List all borrowers |
| `GET` | `/api/borrowers/{id}` | Get one borrower |
| `PUT` | `/api/borrowers/{id}` | Update a borrower |
| `DELETE` | `/api/borrowers/{id}` | Delete a borrower |

Borrower DTO fields are `id` (number), `name` (required non-blank string), `email` (required non-blank valid email), and `phoneNumber` (required non-blank string).

Example create request:

```http
POST /api/borrowers
Content-Type: application/json

{
  "name": "Alex Morgan",
  "email": "alex.morgan@example.com",
  "phoneNumber": "+1-555-0100"
}
```

## Borrowing transactions API

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/transactions` | Borrow a book and create a transaction |
| `GET` | `/api/transactions` | List all transactions |
| `GET` | `/api/transactions/{id}` | Get one transaction |
| `PUT` | `/api/transactions/{id}/return` | Return the book for a transaction |
| `DELETE` | `/api/transactions/{id}` | Delete a transaction |

Transaction DTO fields are `id` (number), `bookId` (required number), `borrowerId` (required number), `borrowDate` (ISO date, assigned by the server on borrow), `returnDate` (ISO date, assigned on return), and `status` (`BORROWED` or `RETURNED`, assigned by the server).

Example borrow request:

```http
POST /api/transactions
Content-Type: application/json

{
  "bookId": 1,
  "borrowerId": 1
}
```

Return a book with:

```http
PUT /api/transactions/1/return
```

The return endpoint has no request body.

## Validation and logging

- Controllers apply `@Valid` to create/update request DTOs.
- Book `title`, `isbn`, and `category` are required and non-blank; `authorId` is required.
- Author `name` is required and non-blank.
- Borrower `name`, `email`, and `phoneNumber` are required and non-blank; `email` must satisfy `@Email`.
- Transaction `bookId` and `borrowerId` are required.
- Services use SLF4J logging. Successful operations and reads are generally logged at `INFO`; missing resources, unavailable books, repeated returns, and similar rejected operations are logged at `WARN`.
- There is no custom validation/error response formatter in the inspected source.

## Borrowing, returning, and deletion behavior

- A new book defaults to available.
- Borrowing an unavailable book is rejected with a runtime exception. A successful borrow marks the book unavailable and creates a transaction with today's date, `BORROWED` status, and no return date.
- Returning a transaction already marked `RETURNED` is rejected. A successful return marks the transaction `RETURNED`, sets today's return date, and marks the book available.
- Deleting an active (`BORROWED`) transaction marks its book available before deleting the transaction. Deleting a returned transaction does not change availability.
- Book, author, and borrower deletes call the repository's `deleteById`. The code does not add explicit relationship checks or custom conflict responses; database foreign-key behavior may affect deletion when related rows exist.

## Known behavior and gaps

1. **Book search is not implemented.** The assignment requires filtering by title, category, or author. The only related route currently present is `GET /api/authors/{id}/books`; no book search or category query parameter is mapped.
2. **Book update availability mapping appears broken.** `BookController.updateBook` does not set availability on the entity it passes to the service. `BookService.updateBook` calls `updatedBook.isAvailable()`, while the DTO's `available` field defaults to `null`. This can cause a null-unboxing error on update. The update behavior should be fixed and verified before relying on `PUT /api/books/{id}`.
3. **Category parsing is case-sensitive.** The controller uses `Category.valueOf`, so values must match the uppercase enum names exactly. Invalid values are not translated into a custom validation message by the inspected code.
4. **Errors have no custom API format.** Services throw `RuntimeException` for missing records, unavailable books, and duplicate returns; the project has no custom exception advice in the inspected source.

