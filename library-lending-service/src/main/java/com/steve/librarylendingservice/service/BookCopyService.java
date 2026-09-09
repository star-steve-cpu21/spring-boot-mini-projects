package com.steve.librarylendingservice.service;

import com.steve.librarylendingservice.dto.AddBookCopyRequestDto;
import com.steve.librarylendingservice.dto.OperationResponse;
import com.steve.librarylendingservice.repository.BookCopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

    @Autowired
    private BookCopyRepository bookCopyRepository;

    public OperationResponse addNewBookCopy(AddBookCopyRequestDto bookCopy) {
        if (bookCopy.getBookId() == null
        || bookCopy.getBarcode() == null) {
            return new OperationResponse(false, "INCOMPLETE_REQ_BODY",
                    "Incomplete request body.", null);
        }
        return bookCopyRepository.insertNewBookCopy(bookCopy);
    }
}
