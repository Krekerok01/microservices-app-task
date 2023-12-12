package com.specificgroup.notification.dto;

import lombok.Getter;

@Getter
public enum MessageType {
    REGISTRATION("Registration"),
    PASSWORD_CHANGE("Password changing"),
    EMAIL_CHANGE("Email changing");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

}
