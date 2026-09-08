package com.steve.librarylendingservice.repository;

import com.steve.librarylendingservice.dto.*;
import com.steve.librarylendingservice.util.Db;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BookRepository {

    @Autowired
    public Db Db;

    public List<BookResponseDto> selectAllBooks() {
        try {
            List<BookResponseDto> bookList = new ArrayList<>();
            var conn = Db.getConn();
            Statement stmt = conn.createStatement();
            String QUERY = """
                SELECT
                b.title, b.isbn, b.description, b.author, b.num_of_pages,
                COUNT(bc.id) AS "num_of_copies"
                FROM book b
                LEFT JOIN book_copy bc ON b.id = bc.book_id
                GROUP BY b.id
                """;
            ResultSet rs = stmt.executeQuery(QUERY);

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String author = rs.getString("author");
                Integer numOfPages = rs.getInt("num_of_pages");
                Integer numOfCopies = rs.getInt("num_of_copies");
                Boolean isAvailable = false;
                if (numOfCopies > 0) {
                    isAvailable = true;
                }

                bookList.add(new BookResponseDto(title, description, isbn, author, numOfPages, numOfCopies, isAvailable));
            }
            return bookList;
        } catch (SQLException e) {
            log.error("Error at BookRepository.selectAllBooks:", e);
            return null;
        }
    }

    public List<BookResponseDto> selectBooksByFilters(BookFilter bookFilter) {
        try {
            List<BookResponseDto> bookList = new ArrayList<>();
            var conn = Db.getConn();
            String QUERY = """
                    SELECT
                    b.title, b.isbn, b.description, b.author, b.num_of_pages,
                    COUNT(bc.id) AS "num_of_copies"
                    FROM book b
                    LEFT JOIN book_copy bc ON b.id = bc.book_id
                    WHERE (? IS NULL OR b.title iLIKE CONCAT('%', ?, '%'))
                      AND (? IS NULL OR b.author iLIKE CONCAT('%', ?, '%'))
                      AND (? IS NULL OR b.isbn = ?)
                      AND (? IS NULL OR b.num_of_pages >= ?)
                      AND (? IS NULL OR b.num_of_pages <= ?)
                    GROUP BY b.id
                """;

            PreparedStatement stmt = conn.prepareStatement(QUERY);

            stmt.setString(1, bookFilter.getTitle());
            stmt.setString(2, bookFilter.getTitle());

            stmt.setString(3, bookFilter.getAuthor());
            stmt.setString(4, bookFilter.getAuthor());

            stmt.setString(5, bookFilter.getIsbn());
            stmt.setString(6, bookFilter.getIsbn());

            if (bookFilter.getMinPages() == null) {
                stmt.setNull(7, 4);
                stmt.setNull(8, 4);
            } else {
                stmt.setInt(7, bookFilter.getMinPages());
                stmt.setInt(8, bookFilter.getMinPages());
            }

            if (bookFilter.getMaxPages() == null) {
                stmt.setNull(9, 4);
                stmt.setNull(10, 4);
            } else {
                stmt.setInt(9, bookFilter.getMaxPages());
                stmt.setInt(10, bookFilter.getMaxPages());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String author = rs.getString("author");
                Integer numOfPages = rs.getInt("num_of_pages");
                Integer numOfCopies = rs.getInt("num_of_copies");
                Boolean isAvailable = false;
                if (numOfCopies > 0) {
                    isAvailable = true;
                }

                bookList.add(new BookResponseDto(title, description, isbn, author, numOfPages, numOfCopies, isAvailable));
            }
            return bookList;
        } catch (SQLException e) {
            log.error("Error at BookRepository.selectBookByFilters: ", e);
            return null;
        }
    }

    public OperationResponse insertNewBook(AddBookRequestDto bookInput) {
        try {
            var conn = Db.getConn();
            String QUERY = """
                    INSERT INTO book(title, description, author, isbn, num_of_pages)
                    VALUES(?, ?, ?, ?, ?)
                """;
            PreparedStatement stmt = conn.prepareStatement(QUERY);

            stmt.setString(1, bookInput.getTitle());
            stmt.setString(2, bookInput.getDescription());
            stmt.setString(3, bookInput.getAuthor());
            stmt.setString(4, bookInput.getIsbn());
            stmt.setInt(5, bookInput.getNumOfPages());
            var rowCount = stmt.executeUpdate();

            if (rowCount == 1) {
                return new OperationResponse(true, "SUCCESS_INSERT_BOOK",
                        "Successful insert book", null);
            } else {
                return new OperationResponse(false, "UNKNOWN_ERROR_INSERT_BOOK",
                        "Failed insert book", null);
            }
        } catch (SQLException e) {
            log.error("Error at BookRepository.insertNewBook: ", e);
            return new OperationResponse(false, "ERROR_INSERT_BOOK",
                    "Failed insert book: " + e, null);
        }
    }

    public OperationResponse insertNewBookCopy(AddBookCopyRequestDto bookCopyInput) {
        try {
            var conn = Db.getConn();
            String QUERY = """
                    INSERT INTO book_copy(book_id, barcode)
                    VALUES(?, ?)
                """;
            PreparedStatement stmt = conn.prepareStatement(QUERY);

            stmt.setObject(1, bookCopyInput.getBookId());
            stmt.setString(2, bookCopyInput.getBarcode());
            var rowCount = stmt.executeUpdate();

            if (rowCount == 1) {
                return new OperationResponse(true, "SUCCESS_INSERT_BOOK_COPY",
                        "Successful insert book copy", null);
            } else {
                return new OperationResponse(false, "UNKNOWN_ERROR_INSERT_BOOK_COPY",
                        "Failed insert book copy", null);
            }
        } catch (SQLException e) {
            log.error("Error at BookRepository.insertNewBookCopy: ", e);
            return new OperationResponse(false, "ERROR_INSERT_BOOK_COPY",
                    "Failed insert book copy: " + e, null);
        }
    }
}