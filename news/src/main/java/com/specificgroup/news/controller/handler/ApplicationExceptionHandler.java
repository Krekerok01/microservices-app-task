package com.specificgroup.news.controller.handler;

import com.specificgroup.news.dto.ExceptionResponse;
import com.specificgroup.news.exception.ReceiveDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ApplicationExceptionHandler {

    @ExceptionHandler(ReceiveDataException.class)
    public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(ReceiveDataException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE), HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ExceptionResponse buildExceptionResponse(String message, HttpStatus httpStatus) {
        return ExceptionResponse.builder()
                .message(message)
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .build();
    }
}