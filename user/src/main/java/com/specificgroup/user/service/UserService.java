package com.specificgroup.user.service;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;

/**
 * Provides methods for processing the input data from the controller and sending it to the repository
 */
public interface UserService {

    /**
     * Find all users from the database
     *
     * @return a list of User objects
     */
    List<User> getAll();

    /**
     * Find user by user id
     *
     * @param id an id of the user
     * @return a list of User objects
     */
    Optional<User> get(final long id);

    /**
     * Find username by user id
     *
     * @param id an id of the user
     * @return a username
     */
    String getUsername(final long id);

    /**
     * Find user by email
     *
     * @param email of the user
     * @return a list of User objects
     */
    Optional<User> getByEmail(final String email);

    /**
     * Create a new user(user registration)
     *
     * @param user an object containing user information
     * @return a saved user object
     */
    User add(final User user);

    /**
     * Delete a user by user id
     *
     * @param id an id of the user being deleted
     */
    void delete(final long id);

    /**
     * Update a user by user id
     *
     * @param id an id of the user being updated
     * @param user an object containing new user information
     */
    void update(final long id, final User user);

    /**
     * Check for the user existence for authentication
     *
     * @param userAuthDto an object containing  user credentials
     * @return TokenResponse an object with token and additional info
     */
    Optional<TokenResponse> jwtTokenOf(final UserAuthDtoRequest userAuthDto);

    /**
     * Check for the user existence for authentication
     *
     * @param email of the user
     * @return UserAuthDtoResponse with user information
     */
    UserAuthDtoResponse checkUserEmail(final String email);

    /**
     * Check the user for existence
     *
     * @param userId an id of the user for checking
     * @return a boolean variable with information about the user's existence
     */
    Boolean existsByUserId(long userId);

    /**
     * Change user role to admin
     *
     * @param userId an id of the user for updating
     */
    void changePrivilege(final long userId);
}
