package com.specificgroup.user.service.impl;

import com.specificgroup.user.exception.DuplicateEmailException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.UserAuthDto;
import com.specificgroup.user.repos.UserRepository;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.JwtGenerator;
import com.specificgroup.user.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KafkaService kafkaService;
    private final JwtGenerator jwtGenerator;
    private final String TOPIC_BLOG_USER = "blog-user";
    private final String TOPIC_SUBSCRIPTION_USER = "subscription-user";

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
    public User add(@Valid User user) {
        if (!checkUserEmailDuplicate(user.getEmail())) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new DuplicateEmailException("User with such email already exists! Please change your email!");
    }


    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
        kafkaService.notify(TOPIC_BLOG_USER, id);
        kafkaService.notify(TOPIC_SUBSCRIPTION_USER, id);
    }

    @Override
    public void update(final long id, final User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        if (!checkUserEmailDuplicate(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(PasswordEncoder.encode(user.getPassword()));
            existingUser.setRole(user.getRole());

            userRepository.save(existingUser);
        } else {
            throw new DuplicateEmailException("User with such email already exists! Please change your email!");
        }
    }

    @Override
    public Optional<String> jwtTokenOf(final UserAuthDto userAuthDto) throws AuthException {
        Optional<User> existingUser = userRepository.findByEmail(userAuthDto.getEmail());

        if (existingUser.isPresent() && PasswordEncoder.encode(userAuthDto.getPassword()).equals(existingUser.get().getPassword())) {
            return Optional.of(jwtGenerator.generate(userAuthDto));
        }
        throw new AuthException();
    }

    @Override
    public UserAuthDto checkUserEmail(final String email) {
        return DtoMapper.mapToUserAuthDto(
                userRepository.findByEmail(email)
                        .orElseThrow(NoSuchElementException::new)
        );
    }

    @Override
    public Boolean existsByUserId(long userId) {
        return userRepository.existsById(userId);
    }

    private boolean checkUserEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
