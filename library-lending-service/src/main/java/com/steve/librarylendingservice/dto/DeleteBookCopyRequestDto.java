package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class DeleteBookCopyRequestDto {
    private UUID id;
}
