package com.specificgroup.user;

import com.specificgroup.user.exception.DuplicateEmailException;
import com.specificgroup.user.exception.NoSuchUserException;
import com.specificgroup.user.exception.WrongPasswordException;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.TokenResponse;
import com.specificgroup.user.model.dto.UserAuthDtoRequest;
import com.specificgroup.user.model.dto.UserUpdateRequest;
import com.specificgroup.user.repos.UserRepository;
import com.specificgroup.user.service.KafkaService;
import com.specificgroup.user.service.impl.UserServiceImpl;
import com.specificgroup.user.util.JwtGenerator;
import com.specificgroup.user.util.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User service test")
public class UserServiceTest {
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaService kafkaService;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    private Logger logger;

    @BeforeEach
    public void init() {
        this.userService = new UserServiceImpl(userRepository, kafkaService, jwtGenerator, logger);
    }

    @Test
    @DisplayName("Test getting all users correctly")
    void testGettingAllUserCorrectly() {
        User userOne = new User(100L, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT);
        User userTwo = new User(200L, "Test username 2", "Test password 2", "Test email 2", User.Role.DEFAULT);
        User userThree = new User(300L, "Test username 3", "Test password 3", "Test email 3", User.Role.ADMIN);

        List<User> users = List.of(userOne, userTwo, userThree);

        doReturn(users).when(userRepository).findAll();

        List<User> allUsers = userService.getAll();

        assertNotNull(allUsers);
        assertEquals(users.size(), allUsers.size());
        assertIterableEquals(users, allUsers);

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting one user correctly")
    void testGettingOneUserCorrectly() {
        long id = 100L;
        doReturn(
                Optional.of(new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT))
        ).when(userRepository).findById(id);

        Optional<User> test = userService.get(id);

        assertNotNull(test);
        assertTrue(test.isPresent());

        User user = test.get();

        assertEquals(user.getId(), id);
        assertEquals(user.getEmail(), "Test email 1");
        assertEquals(user.getPassword(), "Test password 1");
        assertEquals(user.getUsername(), "Test username 1");
        assertEquals(user.getRole(), User.Role.DEFAULT);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting non-existing user")
    void testGettingNonExistingUser() {
        long id = 100L;
        doReturn(Optional.empty()).when(userRepository).findById(id);

        Optional<User> test = userService.get(id);

        assertNotNull(test);
        assertTrue(test.isEmpty());

        assertThrows(NoSuchElementException.class, test::get);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting needed username")
    void testGettingNeededUsername() {
        long id = 100L;
        doReturn(
                Optional.of(new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT))
        ).when(userRepository).findById(id);

        assertEquals("Test username 1", userService.getUsername(id));

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting non-existing username")
    void testGettingNonExistingUsername() {
        long id = 100L;
        doReturn(Optional.empty()).when(userRepository).findById(id);

        assertThrows(NoSuchUserException.class, () -> userService.getUsername(id));

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting user by email")
    void testGettingUserByEmail() {
        String email = "test@test.com";
        long id = 10L;
        doReturn(
                Optional.of(new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT))
        ).when(userRepository).findByEmail(email);

        Optional<User> test = userService.getByEmail(email);

        assertNotNull(test);
        assertTrue(test.isPresent());

        User user = test.get();

        assertEquals(user.getId(), id);
        assertEquals(user.getEmail(), "Test email 1");
        assertEquals(user.getPassword(), "Test password 1");
        assertEquals(user.getUsername(), "Test username 1");
        assertEquals(user.getRole(), User.Role.DEFAULT);

        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting non-existing user by email")
    void testGettingNonExistingUserByEmail() {
        String email = "test@test.com";
        doReturn(
                Optional.empty()
        ).when(userRepository).findByEmail(email);

        Optional<User> test = userService.getByEmail(email);

        assertNotNull(test);
        assertTrue(test.isEmpty());

        assertThrows(NoSuchElementException.class, test::get);

        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test adding new user successfully")
    void testAddingNewUserSuccessfully() {
        long id = 100L;
        User userToAdd = new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT);

        doReturn(Optional.empty()).when(userRepository).findByEmail(any(String.class));
        doReturn(userToAdd).when(userRepository).save(any(User.class));

        User user = userService.add(userToAdd);

        assertEquals(user.getId(), id);
        assertEquals(user.getEmail(), "Test email 1");
        assertEquals(user.getPassword(), "581f92d364a5613d69e9bbd7f8ddc815d62108f00f05299c8606d5592c736f94");
        assertEquals(user.getUsername(), "Test username 1");
        assertEquals(user.getRole(), User.Role.DEFAULT);

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test adding new user with existing email")
    void testAddingNewUserWithExistingEmail() {
        long id = 100L;
        User userToAdd = new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT);

        doReturn(
                Optional.of(new User(id + 1, "Test username 2", "Test password 2", "Test email 1", User.Role.ADMIN))
        ).when(userRepository).findByEmail(any(String.class));

        assertThrows(DuplicateEmailException.class, () -> userService.add(userToAdd));
    }

    @Test
    @DisplayName("Test deleting a user successfully")
    void testDeletingAUserSuccessfully() {
        final long id = 100L;
        final String TOPIC_BLOG_USER = "blog-user";
        final String TOPIC_SUBSCRIPTION_USER = "subscription-user";

        boolean result = assertDoesNotThrow(() -> {
            userService.delete(id);
            return true;
        });

        assertTrue(result);

        verify(userRepository, times(1)).deleteById(id);
        verify(kafkaService, times(1)).notify(TOPIC_BLOG_USER, id);
        verify(kafkaService, times(1)).notify(TOPIC_SUBSCRIPTION_USER, id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating a user successfully")
    void testUpdatingAUserSuccessfully() {
        long id = 100L;
        UserUpdateRequest userToUpdate = new UserUpdateRequest("Test username 1", "Test email 1");

        doReturn(
                Optional.of(new User(id, "Test username 2", "Test password 2", "Test email 2", User.Role.DEFAULT))
        ).when(userRepository).findById(id);
        doReturn(Optional.empty()).when(userRepository).findByEmail(any(String.class));

        assertDoesNotThrow(() -> userService.update(id, userToUpdate));

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating by setting existing email")
    void testUpdatingAUserBySettingExistingEmail() {
        long id = 100L;
        UserUpdateRequest userToUpdate = new UserUpdateRequest("Test username 1", "Test email 1");

        doReturn(
                Optional.of(new User(id, "Test username 2", "Test password 2", "Test email 2", User.Role.DEFAULT))
        ).when(userRepository).findById(id);
        doReturn(
                Optional.of(new User(id, "Test username 2", "Test password 2", "Test email 2", User.Role.DEFAULT))
        ).when(userRepository).findByEmail(any(String.class));

        assertThrows(DuplicateEmailException.class, () -> userService.update(id, userToUpdate));

        verify(userRepository, times(0)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating non-existing user")
    void testUpdatingNonExistingUser() {
        long id = 100L;
        UserUpdateRequest userToUpdate = new UserUpdateRequest("Test username 1", "Test email 1");

        doReturn(
                Optional.empty()
        ).when(userRepository).findById(id);

        assertThrows(NoSuchUserException.class, () -> userService.update(id, userToUpdate));

        verify(userRepository, times(0)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating user password correctly")
    void testUpdatingUserPasswordCorrectly() {
        long id = 100L;
        doReturn(
                Optional.of(
                        new User(
                                id,
                                "Test username 1",
                                "581f92d364a5613d69e9bbd7f8ddc815d62108f00f05299c8606d5592c736f94",
                                "Test email 1",
                                User.Role.DEFAULT)
                )
        ).when(userRepository).findById(id);

        userService.updateUserPassword(id, "Test password 1", "new password");

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating user password incorrectly")
    void testUpdatingUserPasswordIncorrectly() {
        long id = 100L;
        doReturn(
                Optional.of(
                        new User(
                                id,
                                "Test username 1",
                                "some password",
                                "Test email 1",
                                User.Role.DEFAULT)
                )
        ).when(userRepository).findById(id);

        assertThrows(WrongPasswordException.class, () -> userService.updateUserPassword(id, "Test password 1", "new password"));

        verify(userRepository, times(0)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test updating password of a non-existing user")
    void testUpdatingPasswordOfANonExistingUser() {
        long id = 100L;
        doReturn(Optional.empty()).when(userRepository).findById(id);

        assertThrows(NoSuchUserException.class, () -> userService.updateUserPassword(id, "Test password 1", "new password"));

        verify(userRepository, times(0)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test changing existing user role to ADMIN")
    void testChangingExistingUserRoleToAdmin() {
        long id = 100L;
        User userToChangeRole = new User(id, "Test username 1", "Test password 1", "Test email 1", User.Role.DEFAULT);

        doReturn(Optional.of(userToChangeRole)).when(userRepository).findById(id);

        assertDoesNotThrow(() -> userService.changePrivilege(id));

        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test changing non-existing user role to ADMIN")
    void testChangingNonExistingUserRoleToAdmin() {
        long id = 100L;

        doReturn(Optional.empty()).when(userRepository).findById(id);

        assertThrows(NoSuchUserException.class, () -> userService.changePrivilege(id));

        verify(userRepository, times(0)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Test getting a correct token of the user")
    void testGettingCorrectTokenOfTheUser() {
        long id = 1L;
        String username = "Test username";
        UserAuthDtoRequest user = new UserAuthDtoRequest("Test email", "Test password");
        User expectedUser = new User(
                id,
                username,
                user.getPassword(),
                user.getEmail(),
                User.Role.DEFAULT);
        User expectedUserCopy = new User(
                id,
                username,
                "db1bc48640c099bdc6d02c23a9d278edc7024b9eb15fad8925ec7a19f6350a39",
                user.getEmail(),
                User.Role.DEFAULT);

        doReturn(Optional.of(expectedUserCopy)).when(userRepository).findByEmail(any(String.class));

        String expectedToken = jwtGenerator.generate(expectedUser);
        Optional<TokenResponse> actualResponse = userService.jwtTokenOf(user);

        assertNotNull(actualResponse);
        assertTrue(actualResponse.isPresent());

        TokenResponse actual = actualResponse.get();

        assertEquals(id, actual.getUserId());
        assertEquals(expectedToken, actual.getToken());
        assertEquals(username, actual.getUsername());
        assertFalse(actual.isAdmin());
    }
}