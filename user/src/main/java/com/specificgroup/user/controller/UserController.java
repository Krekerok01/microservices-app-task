package com.specificgroup.user.controller;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDto;
import com.specificgroup.user.model.dto.UserDto;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.UtilStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.message.AuthException;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "email", required = false) String userEmail) {
        if (userEmail == null) {
            return DtoMapper.mapToUserDto(userService.getAll());
        } else {
            return DtoMapper.mapToUserDto(List.of(userService.getByEmail(userEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
        }
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable(name = "id") long userId) {
        return DtoMapper.mapToUserDto(
                userService.get(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @PostMapping
    public ResponseEntity<UserDto> newUser(@RequestBody User user) {
        return ResponseEntity.ok(DtoMapper.mapToUserDto(userService.add(user)));
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody UserAuthDto userAuthDto) {
        try {
            return ResponseEntity.of(userService.jwtTokenOf(userAuthDto));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long userId) {
        userService.delete(userId);
        return ResponseEntity
                .status(204)
                .body(UtilStrings.userWasSuccessfullyModified(
                                userId, UtilStrings.Action.DELETED
                        )
                );
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity
                .status(204)
                .body(UtilStrings.userWasSuccessfullyModified(
                                user.getId(), UtilStrings.Action.UPDATED
                        )
                );
    }
}
