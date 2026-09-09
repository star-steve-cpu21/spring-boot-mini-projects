package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class UpdateBookRequestDto {
    private UUID id;
    private String title;
    private String description;
    private String author;
    private String isbn;
    private Integer numOfPages;
}
