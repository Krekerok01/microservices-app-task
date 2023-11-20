package com.specificgroup.subscription.controller.handler;

import com.specificgroup.subscription.dto.ExceptionResponse;
import com.specificgroup.subscription.exception.AccessDeniedException;
import com.specificgroup.subscription.exception.EntityNotFoundException;
import com.specificgroup.subscription.exception.ServiceClientException;
import com.specificgroup.subscription.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionResponse> entityExistsExceptionHandler(EntityExistsException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

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