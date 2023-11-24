package com.specificgroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String username;
    private boolean isAdmin;
}
