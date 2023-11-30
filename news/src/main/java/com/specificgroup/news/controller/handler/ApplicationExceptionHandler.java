package com.specificgroup.news.controller.handler;

import com.specificgroup.news.dto.ExceptionResponse;
import com.specificgroup.news.exception.ReceiveDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Provides methods for error handling
 */
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