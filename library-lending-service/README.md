# Library Lending Service

A small Spring Boot service for a library catalogue — books, physical copies, members and loans.
Currently the book read endpoints are implemented.

## Stack

- Java 21, Spring Boot 4.1.1 (`spring-boot-starter-webmvc`)
- PostgreSQL, accessed through plain JDBC (no JPA / no Spring Data)
- Lombok
- Maven, Docker

## Layout

```
controller/BookController.java   REST endpoints under /api/book
service/BookService.java         pass-through to the repository
repository/BookRepository.java   raw JDBC queries
util/Db.java                     single lazily-created JDBC Connection
dto/BookFilter.java              query params for filtering
dto/BookResponseDto.java         JSON response shape
model/                           Book, BookCopy, Member, Loan
resources/schema.sql             table definitions
```

## Database

Postgres, tables in [schema.sql](src/main/resources/schema.sql):

- `book` — id (uuid), title, description, author, isbn, num_of_pages
- `book_copy` — id (uuid), book_id → book(id), barcode

`Member` and `Loan` exist as Java models only; no tables yet.

Create the DB and apply the schema:

```bash
createdb library_lending_service
psql -d library_lending_service -f src/main/resources/schema.sql
```

## Config

Copy the example and fill in your own connection details:

```bash
cp src/main/resources/application-example.yaml src/main/resources/application.yaml
```

Keys used:

| Key | Meaning |
| --- | --- |
| `db.url` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/library_lending_service` |
| `db.user` | DB user |
| `db.password` | DB password |
| `server.port` | HTTP port (default `8081`) |

## Run

```bash
./mvnw spring-boot:run
```

Package a jar:

```bash
./mvnw clean package
```

Docker (multi-stage build, exposes 8081):

```bash
docker build -t library-lending-service .
```

```bash
docker run -p 8081:8081 library-lending-service
```

Note: the container reads `db.url` from the baked-in `application.yaml`, so `localhost` there
points at the container itself — point it at your host or a DB container when running this way.

## API

See [API_DOC.md](API_DOC.md).
