package com.steve.librarylendingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class Book {
    private final UUID id;
    private String isbn;
    private String title;
    private String description;
    private String author;
    private Integer numOfPages;
}
