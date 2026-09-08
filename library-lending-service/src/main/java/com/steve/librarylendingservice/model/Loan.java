package com.steve.librarylendingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class Loan {
    private final UUID id;
    private final UUID memberId;
    private final UUID bookCopyId;
    private LocalDate lentDate;
    private LocalDate lastReturnDate;
    private String status;
}
