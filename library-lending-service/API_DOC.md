# API Documentation

Base URL: `http://localhost:8081`

All endpoints return JSON.

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

**Response** `200 OK` — same object shape as above, filtered.

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

## Response fields

| Field | Type | Notes |
| --- | --- | --- |
| `title` | string | |
| `description` | string | |
| `isbn` | string | |
| `author` | string | |
| `numOfPages` | integer | |
| `numOfCopies` | integer | count of rows in `book_copy` for this book |
| `isAvailable` | boolean | `true` when `numOfCopies > 0` |

## Errors

There is no error handling layer yet. If a query fails, the repository logs the
`SQLException` and returns `null`, which surfaces as `200 OK` with an empty body.
