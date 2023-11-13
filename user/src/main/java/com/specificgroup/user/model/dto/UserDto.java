package com.specificgroup.user.model.dto;

import com.specificgroup.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private User.Role role;
}
