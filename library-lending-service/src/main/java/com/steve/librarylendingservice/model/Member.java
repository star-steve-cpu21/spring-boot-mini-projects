package com.steve.librarylendingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class Member {
    private final UUID id;
    private String username;
    private String password;
}
