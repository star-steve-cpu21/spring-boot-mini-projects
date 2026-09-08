package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class BookFilter {
    private String isbn;
    private String title;
    private String author;
    private Integer minPages;
    private Integer maxPages;
}
