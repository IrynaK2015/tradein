package com.mytradein.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UnprocessableAdvice {
    @ExceptionHandler(UnprocessableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String unprocessableHandler(UnprocessableException ex) {
        return ex.getMessage();
    }
}
