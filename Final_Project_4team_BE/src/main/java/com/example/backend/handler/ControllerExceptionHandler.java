package com.example.backend.handler;

import com.example.backend.handler.ex.*;
import com.example.backend.web.dto.CMRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 에외를 모아 한번에 처리하는 Controller
 * */
@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * 기본적인 예외 처리방식 예시
     * */
    @ExceptionHandler(CustomApiValidationException.class)
    public ResponseEntity<CMRespDto<?>> apiValidationException(CustomApiValidationException ex) {
        return new ResponseEntity<>(new CMRespDto<>(-1, ex.getMessage(), ex.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiFileUploadException.class)
    public ResponseEntity<CMRespDto<?>> apiFileUploadException(CustomApiFileUploadException ex) {
        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiNotFoundException.class)
    public ResponseEntity<CMRespDto<?>> apiUserNotFoundException(CustomApiNotFoundException ex) {
        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomPageValidationException.class)
    public ResponseEntity<CMRespDto<?>> apiUserNotFoundException(CustomPageValidationException ex) {
        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomNoPermissionException.class)
    public ResponseEntity<CMRespDto<?>> apiNoPermissionException(CustomNoPermissionException ex) {
        return new ResponseEntity<>(new CMRespDto<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
