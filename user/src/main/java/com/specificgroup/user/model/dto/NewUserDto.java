package com.specificgroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @Email(message = "Email pattern should be: *@*.*")
    @NotBlank(message = "Email cannot be empty!")
    private String email;
}
