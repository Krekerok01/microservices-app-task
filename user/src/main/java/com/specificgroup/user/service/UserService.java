package com.specificgroup.user.service;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDto;
import org.springframework.http.ResponseEntity;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> get(final long id);

    Optional<User> getByEmail(final String email);

    User add(final User user);

    void delete(final long id);

    void update(final User user);

    Optional<String> jwtTokenOf(UserAuthDto userAuthDto) throws AuthException;
}
