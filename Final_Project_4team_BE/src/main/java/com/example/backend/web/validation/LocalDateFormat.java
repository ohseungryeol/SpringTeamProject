package com.example.backend.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateFormatValidator.class)
public @interface LocalDateFormat {
    String message() default "날짜 형식이 틀립니다. (ex. 2010-10-10)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
