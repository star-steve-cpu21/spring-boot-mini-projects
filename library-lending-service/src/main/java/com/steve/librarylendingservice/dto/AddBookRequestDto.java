package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class AddBookRequestDto {
    private String title;
    private String description;
    private String author;
    private String isbn;
    private Integer numOfPages;
}
