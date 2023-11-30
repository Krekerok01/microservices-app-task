package com.specificgroup.user.util;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;
import com.specificgroup.user.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Util class for mapping entities and DTO objects
 */
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

    public static UserAuthDtoResponse mapToUserAuthDto(User user) {
        return new UserAuthDtoResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }
}
