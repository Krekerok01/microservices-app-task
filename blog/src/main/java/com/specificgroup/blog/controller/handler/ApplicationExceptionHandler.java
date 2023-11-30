package com.specificgroup.blog.controller.handler;

import com.specificgroup.blog.dto.response.ExceptionResponse;
import com.specificgroup.blog.exception.AccessDeniedException;
import com.specificgroup.blog.exception.EntityNotFoundException;
import com.specificgroup.blog.exception.ServiceClientException;
import com.specificgroup.blog.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods for error handling
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(buildExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> parameterExceptionHandler(
            MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        List<String> errors = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = !errors.isEmpty() ?
                errors.stream().collect(Collectors.joining("; ")):
                "Argument validation failed";

        return new ResponseEntity<>(buildExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> mismatchArgumentTypeExceptionHandler(
            MethodArgumentTypeMismatchException e) {
        String errorMessage = "Incorrect data format for the " + e.getParameter().getParameterName();
        return new ResponseEntity<>(buildExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse buildExceptionResponse(String message) {
        return ExceptionResponse.builder()
                .message(message)
                .build();
    }
}