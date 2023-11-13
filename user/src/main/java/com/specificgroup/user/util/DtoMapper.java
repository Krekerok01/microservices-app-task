package com.specificgroup.user.util;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
    public static List<UserDto> mapToUserDto(List<User> users) {
        return users.stream().map(DtoMapper::mapToUserDto).collect(Collectors.toList());
    }
}
