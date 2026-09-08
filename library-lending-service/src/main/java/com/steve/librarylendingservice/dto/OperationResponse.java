package com.steve.librarylendingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class OperationResponse {
    private Boolean success;
    private String code;
    private String message;
    private Object data;
}
