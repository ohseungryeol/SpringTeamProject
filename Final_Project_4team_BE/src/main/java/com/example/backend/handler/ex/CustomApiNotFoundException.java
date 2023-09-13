package com.example.backend.handler.ex;

import lombok.Getter;

@Getter
public class CustomApiNotFoundException extends RuntimeException{
    public CustomApiNotFoundException(String message) {
        super(message);
    }
}
