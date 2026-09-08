package com.steve.librarylendingservice.service;

import com.steve.librarylendingservice.dto.AddBookRequestDto;
import com.steve.librarylendingservice.dto.BookFilter;
import com.steve.librarylendingservice.dto.BookResponseDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.repository.BookRepository;
import jdk.dynalink.Operation;
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

    public OperationResponse addNewBook(AddBookRequestDto book) {
        if (book.getTitle() == null
        || book.getAuthor() == null
        || book.getDescription() == null
        || book.getIsbn() == null
        || book.getNumOfPages() == null) {
            return new OperationResponse(false, "INCOMPLETE_REQ_BODY",
                    "Incomplete request body.", null);
        }
        return bookRepository.insertNewBook(book);
    }
}
