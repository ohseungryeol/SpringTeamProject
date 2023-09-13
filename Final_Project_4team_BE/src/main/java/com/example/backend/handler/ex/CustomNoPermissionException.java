package com.example.backend.handler.ex;

import lombok.Getter;

@Getter
public class CustomNoPermissionException extends RuntimeException{
    public CustomNoPermissionException(String message) {
        super(message);
    }
}
