package com.steve.librarylendingservice.controller;

import com.steve.librarylendingservice.dto.AddBookCopyRequestDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookcopy")
public class BookCopyController {

    @Autowired
    private BookCopyService bookCopyService;

    @PostMapping("/new")
    public OperationResponse addNewBookCopy(@RequestBody AddBookCopyRequestDto req) {
        return bookCopyService.addNewBookCopy(req);
    }
}
