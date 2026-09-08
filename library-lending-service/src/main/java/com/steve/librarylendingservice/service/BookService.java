package com.steve.librarylendingservice.service;

import com.steve.librarylendingservice.dto.BookFilter;
import com.steve.librarylendingservice.dto.BookResponseDto;
import com.steve.librarylendingservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.selectAllBooks();
    }

    public List<BookResponseDto> getBooksByFilters(BookFilter bookFilter) {
        return bookRepository.selectBooksByFilters(bookFilter);
    }
}
