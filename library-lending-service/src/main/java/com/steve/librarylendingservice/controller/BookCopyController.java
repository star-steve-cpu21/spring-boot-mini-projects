package com.steve.librarylendingservice.controller;

import com.steve.librarylendingservice.dto.AddBookCopyRequestDto;
import com.steve.librarylendingservice.dto.DeleteBookCopyRequestDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.dto.UpdateBookCopyRequestDto;
import com.steve.librarylendingservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookcopy")
public class BookCopyController {

    @Autowired
    private BookCopyService bookCopyService;

    @PostMapping("/new")
    public OperationResponse addNewBookCopy(@RequestBody AddBookCopyRequestDto req) {
        return bookCopyService.addNewBookCopy(req);
    }

    @PutMapping("/update")
    public OperationResponse updateBookCopyById(@RequestBody UpdateBookCopyRequestDto req) {
        return bookCopyService.updateBookCopyById(req);
    }

    @DeleteMapping("/delete")
    public OperationResponse deleteBookCopyById(@RequestBody DeleteBookCopyRequestDto req) {
        return bookCopyService.deleteBookCopyById(req);
    }
}
