package com.specificgroup.blog.controller.handler;

import com.specificgroup.blog.dto.response.ExceptionResponse;
import com.specificgroup.blog.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> parameterExceptionHandler(
            MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        List<String> errors = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = !errors.isEmpty() ? errors.get(0) : "Argument validation failed";

        return new ResponseEntity<>(buildExceptionResponse(errorMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse buildExceptionResponse(String message, HttpStatus httpStatus) {
        return ExceptionResponse.builder()
                .message(message)
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .build();
    }
}