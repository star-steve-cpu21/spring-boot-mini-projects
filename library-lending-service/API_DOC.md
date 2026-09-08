# API Documentation

Base URL: `http://localhost:8081`

All endpoints return JSON. Every endpoint responds `200 OK`; write endpoints report
success or failure inside the body via the [OperationResponse](#operationresponse) shape.

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/book/all` | List all books |
| GET | `/api/book` | List books matching filters |
| POST | `/api/book/new` | Add a book |
| POST | `/api/bookcopy/new` | Add a physical copy of a book |

---

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

## Shapes

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

## Errors

There is no exception handler or non-2xx status mapping. Write endpoints signal failure
through `success` / `code` in the body. On the GET endpoints a `SQLException` is logged and
the repository returns `null`, which surfaces as `200 OK` with an empty body.
