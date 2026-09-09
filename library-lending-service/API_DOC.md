# API Documentation

Base URL: `http://localhost:8081`

All endpoints return JSON. Every endpoint responds `200 OK`; write endpoints report
success or failure inside the body via the [OperationResponse](#operationresponse) shape.

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/book/all` | List all books |
| GET | `/api/book` | List books matching filters |
| POST | `/api/book/new` | Add a book |
| PUT | `/api/book/update` | Update a book by id |
| DELETE | `/api/book/delete` | Delete a book by id |
| POST | `/api/bookcopy/new` | Add a physical copy of a book |
| PUT | `/api/bookcopy/update` | Update a copy's barcode by id |
| DELETE | `/api/bookcopy/delete` | Delete a copy by id |

---

# Book

## `GET /api/book/all`

Returns every book with its copy count.

**Request**

```bash
curl http://localhost:8081/api/book/all
```

**Response** `200 OK`

```json
[
  {
    "title": "The Pragmatic Programmer",
    "description": "From journeyman to master.",
    "isbn": "9780201616224",
    "author": "Andrew Hunt",
    "numOfPages": 352,
    "numOfCopies": 3,
    "isAvailable": true
  }
]
```

---

## `GET /api/book`

Returns books matching the given filters. All parameters are optional; omitting one
skips that condition. With no parameters the result is the same as `/api/book/all`.

**Query parameters**

| Param | Type | Matching |
| --- | --- | --- |
| `title` | string | case-insensitive partial match (`ILIKE %title%`) |
| `author` | string | case-insensitive partial match (`ILIKE %author%`) |
| `isbn` | string | exact match |
| `minPages` | integer | `num_of_pages >= minPages` |
| `maxPages` | integer | `num_of_pages <= maxPages` |

**Request**

```bash
curl "http://localhost:8081/api/book?author=hunt&minPages=200&maxPages=500"
```

**Response** `200 OK` — same object shape as `/api/book/all`, filtered.

---

## `POST /api/book/new`

Inserts a new book. All fields are required — a missing one is rejected with
`INCOMPLETE_REQ_BODY` before touching the database.

**Request body**

| Field | Type | Required |
| --- | --- | --- |
| `title` | string | yes |
| `description` | string | yes |
| `author` | string | yes |
| `isbn` | string | yes |
| `numOfPages` | integer | yes |

```bash
curl -X POST http://localhost:8081/api/book/new \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Pragmatic Programmer",
    "description": "From journeyman to master.",
    "author": "Andrew Hunt",
    "isbn": "9780201616224",
    "numOfPages": 352
  }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_INSERT_BOOK",
  "message": "Successful insert book",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_INSERT_BOOK` | `true` | Row inserted |
| `INCOMPLETE_REQ_BODY` | `false` | A required field was missing |
| `UNKNOWN_ERROR_INSERT_BOOK` | `false` | Insert affected no rows |
| `ERROR_INSERT_BOOK` | `false` | `SQLException` — the exception text is appended to `message` |

---

## `PUT /api/book/update`

Updates a book identified by `id`. The SQL uses `COALESCE(?, column)` per field, so a
`null` field is meant to leave that column unchanged.

**Request body**

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| `id` | UUID | yes | book to update |
| `title` | string | no | |
| `description` | string | no | |
| `author` | string | no | |
| `isbn` | string | no | |
| `numOfPages` | integer | yes in practice | see note below |

```bash
curl -X PUT http://localhost:8081/api/book/update \
  -H "Content-Type: application/json" \
  -d '{
    "id": "3f6b8b2e-6c2a-4f3a-9b1d-2c7a5e8f1a44",
    "title": "The Pragmatic Programmer, 2nd Edition",
    "description": "Your journey to mastery.",
    "author": "Andrew Hunt",
    "isbn": "9780135957059",
    "numOfPages": 352
  }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_UPDATE_BOOK",
  "message": "Successful update book",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_UPDATE_BOOK` | `true` | One row updated |
| `UNKNOWN_ERROR_UPDATE_BOOK` | `false` | No row matched the `id` |
| `ERROR_UPDATE_BOOK` | `false` | `SQLException` — exception text appended to `message` |

**Current behaviour to be aware of**

- `numOfPages` is bound with `setInt`, which cannot take a `null` — omitting it throws
  before the query runs, so send all fields for now.
- The `author` and `isbn` values are bound to each other's placeholders in the SQL, so
  the two columns end up swapped. Send them expecting that until it's fixed.

---

## `DELETE /api/book/delete`

Deletes a book by id. The id is sent in the request body, not the URL.

**Request body**

| Field | Type | Required |
| --- | --- | --- |
| `id` | UUID | yes |

```bash
curl -X DELETE http://localhost:8081/api/book/delete \
  -H "Content-Type: application/json" \
  -d '{ "id": "3f6b8b2e-6c2a-4f3a-9b1d-2c7a5e8f1a44" }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_DELETE_BOOK",
  "message": "Successful delete book",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_DELETE_BOOK` | `true` | One row deleted |
| `UNKNOWN_ERROR_DELETE_BOOK` | `false` | No row matched the `id` |
| `ERROR_DELETE_BOOK` | `false` | `SQLException` — exception text appended to `message` |

`book_copy.book_id` has a foreign key to `book.id`, so deleting a book that still has
copies fails with `ERROR_DELETE_BOOK`. Delete its copies first.

---

# Book copy

## `POST /api/bookcopy/new`

Registers a physical copy of an existing book.

**Request body**

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| `bookId` | UUID | yes | must reference an existing `book.id` |
| `barcode` | string | yes | stored as `CHAR(12)` |

```bash
curl -X POST http://localhost:8081/api/bookcopy/new \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": "3f6b8b2e-6c2a-4f3a-9b1d-2c7a5e8f1a44",
    "barcode": "100000000001"
  }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_INSERT_BOOK_COPY",
  "message": "Successful insert book copy",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_INSERT_BOOK_COPY` | `true` | Row inserted |
| `INCOMPLETE_REQ_BODY` | `false` | `bookId` or `barcode` missing |
| `UNKNOWN_ERROR_INSERT_BOOK_COPY` | `false` | Insert affected no rows |
| `ERROR_INSERT_BOOK_COPY` | `false` | `SQLException` (e.g. unknown `bookId`, barcode not 12 chars) |

---

## `PUT /api/bookcopy/update`

Updates a copy's `barcode`. **`bookId` cannot be changed through this endpoint, by
design.** A copy is a specific physical item of a specific book — repointing it at a
different book would silently rewrite what that item is and leave the shelf, the
barcode and the catalogue disagreeing. If you genuinely need the same barcode to belong
to another book, delete the copy and create a new one under the other `bookId` reusing
the same barcode.

**Request body**

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| `id` | UUID | yes | copy to update |
| `barcode` | string | yes | must be exactly 12 characters |

```bash
curl -X PUT http://localhost:8081/api/bookcopy/update \
  -H "Content-Type: application/json" \
  -d '{
    "id": "9c1f4a77-58d0-4a2e-b8f2-0d3e6b9c7a10",
    "barcode": "100000000002"
  }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_UPDATE_BOOK_COPY",
  "message": "Successful update book copy",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_UPDATE_BOOK_COPY` | `true` | One row updated |
| `INVALID_REQ_BODY` | `false` | `barcode` is not 12 characters |
| `ERROR_UPDATE_BOOK_COPY` | `true` | No row matched the `id` — note `success` is `true` here |
| `ERROR_UPDATE_BOOK_COPY` | `false` | `SQLException` |

`barcode` is also read before the length check, so omitting it throws rather than
returning `INVALID_REQ_BODY`. Always send it.

---

## `DELETE /api/bookcopy/delete`

Deletes a single physical copy by id.

**Request body**

| Field | Type | Required |
| --- | --- | --- |
| `id` | UUID | yes |

```bash
curl -X DELETE http://localhost:8081/api/bookcopy/delete \
  -H "Content-Type: application/json" \
  -d '{ "id": "9c1f4a77-58d0-4a2e-b8f2-0d3e6b9c7a10" }'
```

**Response**

```json
{
  "success": true,
  "code": "SUCCESS_DELETE_BOOK_COPY",
  "message": "Successful delete book copy",
  "data": null
}
```

**Codes**

| `code` | `success` | When |
| --- | --- | --- |
| `SUCCESS_DELETE_BOOK_COPY` | `true` | One row deleted |
| `ERROR_DELETE_BOOK_COPY` | `true` | No row matched the `id` — note `success` is `true` here |
| `ERROR_DELETE_BOOK_COPY` | `false` | `SQLException` |

---

# Shapes

### BookResponseDto

| Field | Type | Notes |
| --- | --- | --- |
| `title` | string | |
| `description` | string | |
| `isbn` | string | |
| `author` | string | |
| `numOfPages` | integer | |
| `numOfCopies` | integer | count of rows in `book_copy` for this book |
| `isAvailable` | boolean | `true` when `numOfCopies > 0` |

### OperationResponse

| Field | Type | Notes |
| --- | --- | --- |
| `success` | boolean | whether the operation completed |
| `code` | string | machine-readable outcome code |
| `message` | string | human-readable detail |
| `data` | object | payload slot, currently always `null` |

# Errors

There is no exception handler or non-2xx status mapping. Write endpoints signal failure
through `success` / `code` in the body. On the GET endpoints a `SQLException` is logged and
the repository returns `null`, which surfaces as `200 OK` with an empty body. An
unhandled runtime exception (see the notes on the two update endpoints) surfaces as
Spring's default `500` error body.
