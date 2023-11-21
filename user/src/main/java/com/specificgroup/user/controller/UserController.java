package com.specificgroup.user.controller;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;
import com.specificgroup.user.model.dto.UserDto;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.UtilStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<UserDto> newUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : bindingResult.getFieldErrors()) {
                sb.append(String.format("%s; ", error.getDefaultMessage()));
            }
            throw new ValidationException(sb.toString());
        }
        return ResponseEntity.ok(DtoMapper.mapToUserDto(userService.add(user)));
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> authenticateUser(@RequestBody UserAuthDtoRequest userAuthDto) {
        try {
            return ResponseEntity.of(userService.jwtTokenOf(userAuthDto));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/auth/{email}")
    public ResponseEntity<UserAuthDtoResponse> authenticateUserByEmail(@PathVariable(name = "email") String email) {
        return ResponseEntity.of(
                Optional.of(
                        userService.checkUserEmail(email)
                )
        );
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody User user) {
        userService.update(userId, user);
        return ResponseEntity
                .status(204)
                .body(UtilStrings.userWasSuccessfullyModified(
                                user.getId(), UtilStrings.Action.UPDATED
                        )
                );
    }
}
