package com.steve.librarylendingservice.repository;

import com.steve.librarylendingservice.model.Book;
import com.steve.librarylendingservice.util.Db;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class BookRepository {

    @Autowired
    public Db Db;

    public List<Book> selectAllBooks() {
        try {
            List<Book> bookList = new ArrayList<>();
            var conn = Db.getConn();
            Statement stmt = conn.createStatement();
            String QUERY = "SELECT * FROM book";
            ResultSet rs = stmt.executeQuery(QUERY);

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String author = rs.getString("author");
                Integer numOfPages = rs.getInt("num_of_pages");

                bookList.add(new Book(id, isbn, title, description, author, numOfPages));
            }
            return bookList;
        } catch (SQLException e) {
            log.error("Error at BookRepository.selectAllBooks: %s", e);
            return null;
        }
    }
}