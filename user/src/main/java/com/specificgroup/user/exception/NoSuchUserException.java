package com.specificgroup.user.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String email) {
        super(String.format("No user with email: %s was found;", email));
    }
}
