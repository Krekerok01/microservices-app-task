package com.specificgroup.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAuthDto {
    private long id;
    private String email;
    private String password;
    private Role role;

    @RequiredArgsConstructor
    @Getter
    public enum Role {
        DEFAULT("Default"),
        ADMIN("Admin");

        private final String name;
    }
}
