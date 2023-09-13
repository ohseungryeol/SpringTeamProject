package com.example.backend.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomApiValidationException extends RuntimeException{
    private Map<String, String> errorMap;

    public CustomApiValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
