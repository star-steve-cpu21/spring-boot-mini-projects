package com.steve.librarylendingservice.controller;

import com.steve.librarylendingservice.dto.*;
import com.steve.librarylendingservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    public BookService bookService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping()
    public ResponseEntity<List<BookResponseDto>> getBooksByFilters(BookFilter bookFilter) {
        return ResponseEntity.ok(bookService.getBooksByFilters(bookFilter));
    }

    @PostMapping("/new")
    public OperationResponse addNewBook(@RequestBody AddBookRequestDto req) {
        return bookService.addNewBook(req);
    }

    @PutMapping("/update")
    public OperationResponse updateBookById(@RequestBody UpdateBookRequestDto req) {
        return bookService.updateBookById(req);
    }

    @DeleteMapping("/delete")
    public OperationResponse deleteBookById(@RequestBody DeleteBookRequestDto req) {
        return bookService.deleteBookById(req);
    }
}
