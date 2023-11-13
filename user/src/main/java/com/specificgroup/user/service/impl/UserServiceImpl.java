package com.specificgroup.user.service.impl;

import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDto;
import com.specificgroup.user.repos.UserRepository;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.JwtGenerator;
import com.specificgroup.user.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User add(User user) {
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(NoSuchElementException::new);

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(PasswordEncoder.encode(user.getPassword()));
        existingUser.setRole(user.getRole());

        userRepository.save(existingUser);
    }

    @Override
    public Optional<String> jwtTokenOf(UserAuthDto userAuthDto) throws AuthException {
        Optional<User> existingUser = userRepository.findByEmail(userAuthDto.getEmail());

        if(existingUser.isPresent() && PasswordEncoder.encode(userAuthDto.getPassword()).equals(existingUser.get().getPassword())) {
            return Optional.of(JwtGenerator.generate(userAuthDto));
        }
        throw new AuthException();
    }
}
