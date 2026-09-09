package com.steve.librarylendingservice.repository;

import com.steve.librarylendingservice.dto.AddBookCopyRequestDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.util.Db;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Repository
public class BookCopyRepository {

    @Autowired
    private Db Db;

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
