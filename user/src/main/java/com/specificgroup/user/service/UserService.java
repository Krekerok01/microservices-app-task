package com.specificgroup.user.service;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDto;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> get(final long id);

    Optional<User> getByEmail(final String email);

    User add(final User user);

    void delete(final long id);

    void update(final long id, final User user);

    Optional<String> jwtTokenOf(final UserAuthDto userAuthDto) throws AuthException;

    UserAuthDto checkUserEmail(final String email);
}
