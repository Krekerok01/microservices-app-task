package com.specificgroup.user.service;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> get(final long id);

    String getUsername(final long id);

    Optional<User> getByEmail(final String email);

    User add(final User user);

    void delete(final long id);

    void update(final long id, final User user);

    Optional<TokenResponse> jwtTokenOf(final UserAuthDtoRequest userAuthDto);

    UserAuthDtoResponse checkUserEmail(final String email);

    Boolean existsByUserId(long userId);

    void changePrivilege(final long userId);
}
