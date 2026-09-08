package com.steve.librarylendingservice.service;

import com.steve.librarylendingservice.model.Book;
import com.steve.librarylendingservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.selectAllBooks();
    }
}
