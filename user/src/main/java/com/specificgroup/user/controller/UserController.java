package com.specificgroup.user.controller;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;
import com.specificgroup.user.model.dto.UserDto;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.UtilStrings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Getting all users or getting one user by email", description = "Getting all users(or one user by email) from the database")
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

    @Operation(summary = "Getting user by id", description = "Getting user info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public UserDto get(@PathVariable(name = "id") long userId) {
        return DtoMapper.mapToUserDto(
                userService.get(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @Operation(summary = "Registering a new user", description = "Registering a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content)})
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
        try {
            return ResponseEntity.of(userService.jwtTokenOf(userAuthDto));
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Operation(summary = "Getting UserAuthDtoResponse", description = "Used for user authentication")
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

    @Operation(summary = "Deletion user by id", description = "Deletion user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
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

    @Operation(summary = "Updating user", description = "Updating user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: There is no such user in the database or Error: Existing email",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
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

    @Operation(summary = "Checking for the existence of a user", description = "Checking for the existence of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/exists/{id}")
    public Boolean existsByUserId(@PathVariable(name = "id") long userId) {
        return userService.existsByUserId(userId);
    }
}
