package com.specificgroup.user.controller;

import com.specificgroup.user.exception.NoPrivilegesException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.*;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.JwtParser;
import com.specificgroup.user.util.UtilStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
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
    public User get(@PathVariable(name = "id") long userId,
                    HttpServletRequest request) {
        if (getUserIdFromToken(request) == userId) {
            return userService.get(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } else {
            throw new NoPrivilegesException();
        }
    }

    @GetMapping("/{id}/username")
    public UsernameResponse getUsername(@PathVariable(name = "id") long userId) {
        return new UsernameResponse(userService.getUsername(userId));
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
        return ResponseEntity.of(userService.jwtTokenOf(userAuthDto));
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
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long userId, HttpServletRequest request) throws AuthException {
        if (getUserIdFromToken(request) == userId || getRoleFromToken(request).equals(User.Role.ADMIN)) {
            userService.delete(userId);
            return ResponseEntity
                    .status(204)
                    .body(UtilStrings.userWasSuccessfullyModified(
                                    userId, UtilStrings.Action.DELETED
                            )
                    );
        }
        throw new NoPrivilegesException();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody User user,
                                             HttpServletRequest request) {
        if (getUserIdFromToken(request) == userId) {
            userService.update(userId, user);
            return ResponseEntity
                    .status(204)
                    .body(UtilStrings.userWasSuccessfullyModified(
                                    user.getId(), UtilStrings.Action.UPDATED
                            )
                    );
        } else {
            throw new NoPrivilegesException();
        }
    }

    @PutMapping("/privilege/{userId}")
    public void changePrivilege(@PathVariable(name = "userId") long userId, HttpServletRequest request) {
        if (getRoleFromToken(request).equals(User.Role.ADMIN)) {
            userService.changePrivilege(userId);
        } else {
            throw new NoPrivilegesException();
        }
    }

    @GetMapping("/exists/{id}")
    public Boolean existsByUserId(@PathVariable(name = "id") long userId) {
        return userService.existsByUserId(userId);
    }

    private long getUserIdFromToken(HttpServletRequest request) {
        return JwtParser.getUserIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private User.Role getRoleFromToken(HttpServletRequest request) {
        return User.Role.valueOf(JwtParser.getRoleFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)));
    }
}
