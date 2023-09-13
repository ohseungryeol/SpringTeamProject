package com.example.backend.util;

import com.example.backend.handler.ex.CustomApiValidationException;
import com.example.backend.handler.ex.CustomPageValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ValidCheck {
    public static void validCheck(BindingResult bindingResult) throws CustomApiValidationException {
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            throw new CustomApiValidationException("잘못된 요청값입니다", errorMap);
        }
    }

    public static void pageValidCheck(Integer page, Integer limit) {
        if(page < 0 || limit < 1) throw new CustomPageValidationException("잘못된 페이징값 입니다");
    }
}
