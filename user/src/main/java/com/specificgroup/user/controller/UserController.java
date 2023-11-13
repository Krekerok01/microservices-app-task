package com.specificgroup.user.controller;

import com.specificgroup.user.model.User;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.UtilStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers(@RequestParam(name = "email", required = false) String userEmail) {
        if (userEmail == null) {
            return userService.getAll();
        } else {
            return List.of(userService.getByEmail(userEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        }
    }

    @GetMapping("/{id}")
    public User get(@PathVariable(name = "id") long userId) {
        return userService.get(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<User> newUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.add(user));
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
