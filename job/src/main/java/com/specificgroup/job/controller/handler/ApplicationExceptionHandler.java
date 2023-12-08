package com.specificgroup.job.controller.handler;

import com.specificgroup.job.dto.ExceptionResponse;
import com.specificgroup.job.exception.ReceiveDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Provides methods for error handling
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ReceiveDataException.class)
    public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(ReceiveDataException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ExceptionResponse buildExceptionResponse(String message) {
        return ExceptionResponse.builder()
                .message(message)
                .build();
    }
}