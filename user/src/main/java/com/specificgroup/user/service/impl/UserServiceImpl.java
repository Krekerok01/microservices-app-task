package com.specificgroup.user.service.impl;

import com.specificgroup.user.exception.DuplicateEmailException;
import com.specificgroup.user.exception.NoSuchUserException;
import com.specificgroup.user.exception.WrongPasswordException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;
import com.specificgroup.user.repos.UserRepository;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.JwtGenerator;
import com.specificgroup.user.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KafkaService kafkaService;
    private final JwtGenerator jwtGenerator;
    private final String TOPIC_BLOG_USER = "blog-user";
    private final String TOPIC_SUBSCRIPTION_USER = "subscription-user";

    @Override
    public List<User> getAll() {
        log.info("Getting all users.");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }

    @Override
    public String getUsername(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(NoSuchUserException::new);

        return user.getUsername();
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
        throw new DuplicateEmailException("User with such email already exists!;");
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
                .orElseThrow(NoSuchUserException::new);
        String email = user.getEmail();

        if (
                Objects.equals(email, existingUser.getEmail())
                        ||
                        (
                                !Objects.equals(
                                        email, existingUser.getEmail())
                                        &&
                                        !checkUserEmailDuplicate(email)
                        )
        ) {
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
    public void changePrivilege(final long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);

        if (!existingUser.getRole().equals(User.Role.ADMIN)) {
            existingUser.setRole(User.Role.ADMIN);
            userRepository.save(existingUser);
        }
    }

    @Override
    public Optional<TokenResponse> jwtTokenOf(final UserAuthDtoRequest userAuthDto) {
        Optional<User> existingUser = userRepository.findByEmail(userAuthDto.getEmail());

        if (existingUser.isPresent()
                &&
                PasswordEncoder.encode(
                                userAuthDto.getPassword())
                        .equals(
                                existingUser
                                        .get()
                                        .getPassword()
                        )
        ) {
            User user = existingUser.get();
            boolean isAdmin = user.getRole().equals(User.Role.ADMIN);
            return Optional.of(
                    new TokenResponse(
                            user.getId(),
                            jwtGenerator.generate(user),
                            user.getUsername(),
                            isAdmin
                    )
            );
        } else if (existingUser.isEmpty()) {
            throw new NoSuchUserException(userAuthDto.getEmail());
        }
        throw new WrongPasswordException(String.format("Wrong password for user %s;", userAuthDto.getEmail()));
    }

    @Override
    public UserAuthDtoResponse checkUserEmail(final String email) {
        return DtoMapper.mapToUserAuthDto(
                userRepository.findByEmail(email)
                        .orElseThrow(NoSuchUserException::new)
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
