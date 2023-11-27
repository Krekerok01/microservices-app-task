package com.specificgroup.user.controller.exceptionHandle;

import com.specificgroup.user.exception.DuplicateEmailException;
import com.specificgroup.user.exception.NoPrivilegesException;
import com.specificgroup.user.exception.NoSuchUserException;
import com.specificgroup.user.exception.WrongPasswordException;
import com.specificgroup.user.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(DuplicateEmailException dee) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(dee.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException ve) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ve.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEmailException(NoSuchUserException nsue) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(nsue.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleWrongPasswordException(WrongPasswordException wpe) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(wpe.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoPrivilegesException.class)
    public ResponseEntity<ExceptionResponse> handleNoPrivilegesException(NoPrivilegesException npe) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(npe.getMessage()));
    }
}
