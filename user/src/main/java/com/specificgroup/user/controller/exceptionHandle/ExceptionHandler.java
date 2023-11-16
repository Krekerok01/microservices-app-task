package com.specificgroup.user.controller.exceptionHandle;

import com.specificgroup.user.exception.DuplicateEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException dee) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dee.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ve) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getMessage());
    }
}
