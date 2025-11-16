package com.codifytech.hospitalManagement.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
public class ErrorResponse {
    private String message;
    private HttpStatus statusCode;
    private LocalDateTime timeStamp;

    public ErrorResponse(){
        timeStamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, HttpStatus statusCode) {
        this();
        this.message = message;
        this.statusCode = statusCode;
    }
}
