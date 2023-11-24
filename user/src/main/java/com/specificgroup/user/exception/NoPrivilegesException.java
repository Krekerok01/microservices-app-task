package com.specificgroup.user.exception;

public class NoPrivilegesException extends RuntimeException {
    public NoPrivilegesException() {
        super("You have no rights to perform such an action!");
    }
}
