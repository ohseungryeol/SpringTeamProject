package com.example.backend.handler.ex;

import lombok.Getter;

@Getter
public class CustomPageValidationException extends RuntimeException{
    public CustomPageValidationException(String message) {
        super(message);
    }
}
