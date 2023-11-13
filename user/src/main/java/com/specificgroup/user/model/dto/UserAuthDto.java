package com.specificgroup.user.model.dto;

import com.specificgroup.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAuthDto {
    private long id;
    private String email;
    private String password;
    private User.Role role;
}
