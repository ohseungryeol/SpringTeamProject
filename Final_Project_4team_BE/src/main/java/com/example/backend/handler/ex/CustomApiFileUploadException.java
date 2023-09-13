package com.example.backend.handler.ex;

import lombok.Getter;

@Getter
public class CustomApiFileUploadException extends RuntimeException{
    public CustomApiFileUploadException(String message) {
        super(message);
    }
}
