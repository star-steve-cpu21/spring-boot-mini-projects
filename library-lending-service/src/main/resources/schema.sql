CREATE TABLE "book"(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    num_of_pages INTEGER NOT NULL
);

CREATE TABLE "book_copy"(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL,
    barcode CHAR(12) NOT NULL,
    FOREIGN KEY(book_id) REFERENCES book(id)
);
