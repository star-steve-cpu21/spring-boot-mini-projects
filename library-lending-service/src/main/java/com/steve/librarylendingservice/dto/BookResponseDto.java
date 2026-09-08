package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class BookResponseDto {
    private String title;
    private String description;
    private String isbn;
    private String author;
    private Integer numOfPages;
    private Integer numOfCopies;
    private Boolean isAvailable;
}
