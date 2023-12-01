package com.specificgroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PasswordRequestDto {

    @NotBlank(message = "Password must not be blank;")
    private String currentPassword;

    @NotBlank(message = "Password must not be blank;")
    private String newPassword;
}