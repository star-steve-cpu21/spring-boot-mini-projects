package com.steve.librarylendingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class BookCopy {
    private final UUID id;
    private final UUID bookId;
    private String barcode;
}
