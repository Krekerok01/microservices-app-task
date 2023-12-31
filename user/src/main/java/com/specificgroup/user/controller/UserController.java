package com.specificgroup.user.controller;

import com.specificgroup.user.exception.NoPrivilegesException;
import com.specificgroup.user.exception.NoSuchUserException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.*;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.JwtParser;
import com.specificgroup.user.util.UtilStrings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users or get one user by email", description = "Getting all users(or one user by email) from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There are no users in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "email", required = false) String userEmail) {
        if (userEmail == null) {
            return DtoMapper.mapToUserDto(userService.getAll());
        } else {
            return DtoMapper.mapToUserDto(List.of(userService.getByEmail(userEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
        }
    }

    @Operation(summary = "Get user by id", description = "Getting user info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public User get(@PathVariable(name = "id") long userId,
                    HttpServletRequest request) throws AuthException {
        if (getUserIdFromToken(request) == userId) {
            return userService.get(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } else {
            throw new NoPrivilegesException();
        }
    }

    @Operation(summary = "Get username by user id", description = "Getting username by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}/username")
    public UsernameResponse getUsername(@PathVariable(name = "id") long userId) {
        return new UsernameResponse(userService.getUsername(userId));
    }

    @Operation(summary = "Register a new user", description = "Registering a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<UserDto> newUser(@RequestBody @Valid NewUserDto user, BindingResult bindingResult) {
        validateEntity(bindingResult);
        return ResponseEntity.ok(DtoMapper.mapToUserDto(userService.add(user)));
    }

    @Operation(summary = "User authentication", description = "User authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "Error: Incorrect credentials)",
                    content = @Content)})
    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> authenticateUser(@RequestBody UserAuthDtoRequest userAuthDto) {
        return ResponseEntity.of(userService.jwtTokenOf(userAuthDto));
    }

    @Operation(summary = "Get UserAuthDtoResponse", description = "Used for user authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: There is no such user in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/{email}")
    public ResponseEntity<UserAuthDtoResponse> authenticateUserByEmail(@PathVariable(name = "email") String email) {
        return ResponseEntity.of(
                Optional.of(
                        userService.checkUserEmail(email)
                )
        );
    }

    @Operation(summary = "Delete user by id", description = "Deletion user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: This action can be performed by the user himself or the admin",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long userId, HttpServletRequest request) throws AuthException {
        if (getUserIdFromToken(request) == userId || getRoleFromToken(request).equals(User.Role.ADMIN)) {
            if (userService.existsByUserId(userId)) {
                userService.delete(userId);
                return ResponseEntity
                        .status(204)
                        .body(UtilStrings.userWasSuccessfullyModified(
                                        userId, UtilStrings.Action.DELETED
                                )
                        );
            } else {
                throw new NoSuchUserException();
            }
        }
        throw new NoPrivilegesException();
    }

    @Operation(summary = "Update user", description = "Updating user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: There is no such user in the database or Error: Existing email",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: User can only manage his own information",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "id") long userId,
                                             @RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                             BindingResult bindingResult,
                                             HttpServletRequest request
    ) throws AuthException {
        validateEntity(bindingResult);
        if (getUserIdFromToken(request) == userId) {
            userService.update(userId, userUpdateRequest);
            return ResponseEntity
                    .status(200)
                    .body(UtilStrings.userWasSuccessfullyModified(
                                    userId, UtilStrings.Action.UPDATED
                            )
                    );
        } else {
            throw new NoPrivilegesException();
        }
    }

    @Operation(summary = "Update user password", description = "Updating user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: There is no such user in the database or Error: Existing email",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: User can only manage his own information",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/password/{id}")
    public ResponseEntity<String> updateUserPassword(@PathVariable(name = "id") long userId,
                                                     @RequestBody @Valid PasswordRequestDto password,
                                                     BindingResult bindingResult,
                                                     HttpServletRequest request) throws AuthException {
        validateEntity(bindingResult);
        if (getUserIdFromToken(request) == userId) {
            userService.updateUserPassword(userId, password.getCurrentPassword(), password.getNewPassword());
            return ResponseEntity
                    .status(200)
                    .body(UtilStrings.userWasSuccessfullyModified(
                                    userId, UtilStrings.Action.UPDATED
                            )
                    );
        } else {
            throw new NoPrivilegesException();
        }
    }

    @Operation(summary = "Change user role to admin", description = "Changing user role to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Only the administrator has the right to perform this action",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/privilege/{userId}")
    public void changePrivilege(@PathVariable(name = "userId") long userId) {
        userService.changePrivilege(userId);
    }

    @Operation(summary = "Check for the existence of a user", description = "Checking for the existence of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/exists/{id}")
    public boolean existsByUserId(@PathVariable(name = "id") long userId) {
        return userService.existsByUserId(userId);
    }

    private long getUserIdFromToken(HttpServletRequest request) throws AuthException {
        return JwtParser.getUserIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private User.Role getRoleFromToken(HttpServletRequest request) throws AuthException {
        return User.Role.valueOf(JwtParser.getRoleFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)));
    }

    private void validateEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : bindingResult.getFieldErrors()) {
                sb.append(String.format("%s; ", error.getDefaultMessage()));
            }
            throw new ValidationException(sb.toString());
        }
    }
}
