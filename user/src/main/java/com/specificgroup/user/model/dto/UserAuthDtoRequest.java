package com.specificgroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAuthDtoRequest {
    private String email;
    private String password;
}
