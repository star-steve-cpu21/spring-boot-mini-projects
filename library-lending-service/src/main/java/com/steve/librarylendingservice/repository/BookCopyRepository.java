package com.steve.librarylendingservice.repository;

import com.steve.librarylendingservice.dto.AddBookCopyRequestDto;
import com.steve.librarylendingservice.dto.DeleteBookCopyRequestDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.dto.UpdateBookCopyRequestDto;
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
            log.error("Error at BookCopyRepository.insertNewBookCopy: ", e);
            return new OperationResponse(false, "ERROR_INSERT_BOOK_COPY",
                    "Failed insert book copy: " + e, null);
        }
    }

    public OperationResponse updateBookCopyById(UpdateBookCopyRequestDto bookCopyInput) {
        try {
            var conn = Db.getConn();
            String QUERY = """
                    UPDATE book_copy
                    SET barcode = ?
                    WHERE id = ?
                """;
            PreparedStatement stmt = conn.prepareStatement(QUERY);
            stmt.setString(1, bookCopyInput.getBarcode());
            stmt.setObject(2, bookCopyInput.getId());

            var rowCount = stmt.executeUpdate();

            if (rowCount == 1) {
                return new OperationResponse(true, "SUCCESS_UPDATE_BOOK_COPY",
                        "Successful update book copy", null);
            } else {
                return new OperationResponse(true, "ERROR_UPDATE_BOOK_COPY",
                        "Failed to update book copy", null);
            }
        } catch (SQLException e) {
            log.error("Error at BookCopyRepository.updateBookCopyById: ", e);
            return new OperationResponse(false, "ERROR_UPDATE_BOOK_COPY",
                    "Failed to update book copy", null);
        }
    }

    public OperationResponse deleteBookCopyById(DeleteBookCopyRequestDto bookCopyInput) {
        try {
            var conn = Db.getConn();
            String QUERY = """
                    DELETE FROM book_copy
                    WHERE id = ?
                """;
            PreparedStatement stmt = conn.prepareStatement(QUERY);
            stmt.setObject(1, bookCopyInput.getId());

            var rowCount = stmt.executeUpdate();

            if (rowCount == 1) {
                return new OperationResponse(true, "SUCCESS_DELETE_BOOK_COPY",
                        "Successful delete book copy", null);
            } else {
                return new OperationResponse(true, "ERROR_DELETE_BOOK_COPY",
                        "Failed to delete book copy", null);
            }
        } catch (SQLException e) {
            log.error("Error at BookCopyRepository.deleteBookCopyById: ", e);
            return new OperationResponse(false, "ERROR_DELETE_BOOK_COPY",
                    "Failed to delete book copy", null);
        }
    }
}
