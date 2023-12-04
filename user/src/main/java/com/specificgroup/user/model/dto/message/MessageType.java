package com.specificgroup.user.model.dto.message;

import lombok.Getter;

@Getter
public enum MessageType {
    REGISTRATION("Registration"),
    PASSWORD_CHANGE("Password changing");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

}
