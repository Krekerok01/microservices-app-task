package com.specificgroup.gateway.controller.handler;

import com.specificgroup.gateway.dto.ExceptionResponse;
import com.specificgroup.gateway.exception.ServiceClientException;
import com.specificgroup.gateway.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Provides methods for error handling
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionResponse> serviceUnavailableExceptionHandler(ServiceUnavailableException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ServiceClientException.class)
    public ResponseEntity<ExceptionResponse> serviceClientExceptionHandler(ServiceClientException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse buildExceptionResponse(String message) {
        return ExceptionResponse.builder()
                .message(message)
                .build();
    }
}