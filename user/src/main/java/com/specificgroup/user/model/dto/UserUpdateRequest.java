package com.specificgroup.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Username cannot be blank!;")
    private String username;

    @Email(message = "Email pattern should be: *@*.*;")
    @NotBlank(message = "Email cannot be empty!;")
    private String email;
}