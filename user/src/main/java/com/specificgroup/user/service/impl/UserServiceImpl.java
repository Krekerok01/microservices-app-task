package com.specificgroup.user.service.impl;

import com.specificgroup.user.exception.DuplicateEmailException;
import com.specificgroup.user.exception.NoSuchUserException;
import com.specificgroup.user.exception.WrongPasswordException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.*;
import com.specificgroup.user.model.dto.message.MessageType;
import com.specificgroup.user.repos.UserRepository;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.service.UserService;
import com.specificgroup.user.util.DtoMapper;
import com.specificgroup.user.util.JwtGenerator;
import com.specificgroup.user.util.Logger;
import com.specificgroup.user.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KafkaService kafkaService;
    private final JwtGenerator jwtGenerator;
    private final Logger logger;

    @Value("${spring.kafka.topics.blog-user}")
    private String TOPIC_BLOG_USER;

    @Value("${spring.kafka.topics.subscription-user}")
    private String TOPIC_SUBSCRIPTION_USER;

    @Value("${spring.kafka.topics.user-registration}")
    private String TOPIC_USER_REGISTRATION;

    @Value("${spring.kafka.topics.user-password-change}")
    private String TOPIC_USER_PASSWORD_CHANGE;

    @Value("${spring.kafka.topics.user-email-change}")
    private String TOPIC_USER_EMAIL_CHANGE;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        logger.info("Getting all users.");
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> get(long id) {
        logger.info("Getting a user with id " + id);
        return userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(NoSuchUserException::new);

        return user.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getByEmail(String email) {
        logger.info("Getting a user with email " + email);
        return userRepository.findByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User add(@Valid NewUserDto user) {
        if (!checkUserEmailDuplicate(user.getEmail())) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
            logger.info(String.format("Saving a new user with email %s to the database", user.getEmail()));
            kafkaService.notify(TOPIC_USER_REGISTRATION, user.getUsername(), user.getEmail(), MessageType.REGISTRATION);
            return userRepository.save(DtoMapper.mapToUser(user));
        }
        throw new DuplicateEmailException("User with such email already exists!;");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
        kafkaService.notify(TOPIC_BLOG_USER, id);
        kafkaService.notify(TOPIC_SUBSCRIPTION_USER, id);
        logger.info("Deleting a user with id " + id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final long id, final UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(NoSuchUserException::new);
        String email = userUpdateRequest.getEmail();
        String username = userUpdateRequest.getUsername();

        if (!Objects.equals(
                email, existingUser.getEmail()) || !Objects.equals(
                username, existingUser.getUsername())) {
            if (!Objects.equals(
                    email, existingUser.getEmail())
            ) {
                if (!checkUserEmailDuplicate(email)) {
                    kafkaService.notify(TOPIC_USER_EMAIL_CHANGE, existingUser.getUsername(), email, MessageType.EMAIL_CHANGE);
                    existingUser.setEmail(userUpdateRequest.getEmail());
                } else {
                    throw new DuplicateEmailException("User with such email already exists! Please change your email!;");
                }
            }
            if (!Objects.equals(
                    username, existingUser.getUsername())
            ) {
                existingUser.setUsername(username);
            }

            logger.info("Updating a user with id " + id);
            userRepository.save(existingUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserPassword(long id, String currentPassword, String newPassword) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(NoSuchUserException::new);

        if (!PasswordEncoder.encode(currentPassword).equals(existingUser.getPassword()))
            throw new WrongPasswordException("Wrong password!;");

        existingUser.setPassword(PasswordEncoder.encode(newPassword));

        logger.info("Updating a password of user with id " + id);
        kafkaService.notify(TOPIC_USER_PASSWORD_CHANGE, existingUser.getUsername(), existingUser.getEmail(), MessageType.PASSWORD_CHANGE);
        userRepository.save(existingUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePrivilege(final long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);

        if (!existingUser.getRole().equals(User.Role.ADMIN)) {
            existingUser.setRole(User.Role.ADMIN);
            logger.info(String.format("Changed role of user with id %d to ADMIN", userId));
            userRepository.save(existingUser);
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAuthDtoResponse checkUserEmail(final String email) {
        return DtoMapper.mapToUserAuthDto(
                userRepository.findByEmail(email)
                        .orElseThrow(NoSuchUserException::new)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUserId(final long userId) {
        return userRepository.existsById(userId);
    }


    private boolean checkUserEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
