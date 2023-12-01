package com.specificgroup.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.specificgroup.user.controller.UserController;
import com.specificgroup.user.model.User;
import com.specificgroup.user.model.dto.PasswordRequestDto;
import com.specificgroup.user.model.dto.UserAuthDtoResponse;
import com.specificgroup.user.model.dto.UserDto;
import com.specificgroup.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private JacksonTester<PasswordRequestDto> passwordRequestDto;

    @Autowired
    private JacksonTester<User> userRequestDto;
    static Gson gson;

    @BeforeAll
    static void init() {
        gson = new Gson();
    }

    @Test
    @DisplayName("Test getting all users")
    public void testGettingAllUsers() throws Exception {
        List<User> users = List.of(
                new User(1L, "test1_username", "test1_password", "email1", User.Role.DEFAULT),
                new User(2L, "test2_username", "test2_password", "email2", User.Role.DEFAULT)
        );
        doReturn(users).when(userService).getAll();
        MockHttpServletResponse response = mvc.perform(get("/users")).andReturn().getResponse();

        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        List<UserDto> usersResponse = gson.fromJson(response.getContentAsString(), listType);

        assertEquals(200, response.getStatus());
        assertEquals(2, usersResponse.size());
        assertEquals("test1_username", usersResponse.get(0).getUsername());
        assertEquals("email2", usersResponse.get(1).getEmail());
    }

    @Test
    @DisplayName("Test getting one user by email")
    public void testGettingUserByEmail() throws Exception {
        User expected = new User(1L, "test1_username", "test1_password", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).getByEmail(any(String.class));
        MockHttpServletResponse response = mvc.perform(get("/users?email=email")).andReturn().getResponse();

        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        List<UserDto> usersResponse = gson.fromJson(response.getContentAsString(), listType);

        assertEquals(200, response.getStatus());
        assertEquals(1, usersResponse.size());
        assertEquals("test1_username", usersResponse.get(0).getUsername());
        assertEquals("email1", usersResponse.get(0).getEmail());
        assertEquals(1L, usersResponse.get(0).getId());
        assertEquals(User.Role.DEFAULT, usersResponse.get(0).getRole());
    }

    @Test
    @DisplayName("Test getting non-existing user by email")
    public void testGettingNonExistingUserByEmail() throws Exception {
        doReturn(Optional.empty()).when(userService).getByEmail(any(String.class));
        MockHttpServletResponse response = mvc.perform(get("/users?email=email")).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }


    @Test
    @DisplayName("Test getting user by id")
    public void testGettingUserById() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).get(id);
        MockHttpServletResponse response = mvc.perform(
                get("/users/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
        ).andReturn().getResponse();

        Type type = new TypeToken<User>() {
        }.getType();
        User userResponse = gson.fromJson(response.getContentAsString(), type);

        assertEquals(200, response.getStatus());
        assertEquals("test1_username", userResponse.getUsername());
        assertEquals("email1", userResponse.getEmail());
        assertEquals("test1_password", userResponse.getPassword());
        assertEquals(1L, userResponse.getId());
        assertEquals(User.Role.DEFAULT, userResponse.getRole());
    }

    @Test
    @DisplayName("Test getting non-existing user by id")
    public void testGettingNonExistingUserById() throws Exception {
        doReturn(Optional.empty()).when(userService).get(1);
        MockHttpServletResponse response = mvc.perform(
                get("/users/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("Test making unauthorized request")
    public void testMakingUnauthorizedRequest() {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).get(id);
        assertThrows(NestedServletException.class, () -> mvc.perform(
                get("/users/1")
        ).andReturn().getResponse());
    }

    @Test
    @DisplayName("Test password checking")
    public void testPasswordChecking() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).get(id);
        MockHttpServletResponse response = mvc.perform(
                post("/users/passwordValidation")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(passwordRequestDto.write(new PasswordRequestDto("password")).getJson())
        ).andReturn().getResponse();


        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Test password checking with the wrong input")
    public void testWrongPasswordChecking() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "wrong_value", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).get(id);
        MockHttpServletResponse response = mvc.perform(
                post("/users/passwordValidation")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(passwordRequestDto.write(new PasswordRequestDto("password")).getJson())
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Test password checking by the unauthorized user")
    public void testUnauthorizedPasswordChecking() {
        long id = 1L;
        User expected = new User(id, "test1_username", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", "email1", User.Role.DEFAULT);
        doReturn(Optional.of(expected)).when(userService).get(id);
        assertThrows(NestedServletException.class, () -> mvc.perform(
                post("/users/passwordValidation")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(passwordRequestDto.write(new PasswordRequestDto("password")).getJson())
        ).andReturn().getResponse());
    }

    @Test
    @DisplayName("Test adding new user correctly")
    public void testAddingNewUser() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "email1@email.com", User.Role.DEFAULT);
        doReturn(expected).when(userService).add(any(User.class));
        MockHttpServletResponse response = mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userRequestDto.write(expected).getJson())
        ).andReturn().getResponse();

        Type type = new TypeToken<UserDto>() {
        }.getType();
        UserDto userResponse = gson.fromJson(response.getContentAsString(), type);

        assertEquals(200, response.getStatus());
        assertEquals("test1_username", userResponse.getUsername());
        assertEquals("email1@email.com", userResponse.getEmail());
        assertEquals(id, userResponse.getId());
        assertEquals(User.Role.DEFAULT, userResponse.getRole());
    }

    @Test
    @DisplayName("Test adding incorrect user data")
    public void testAddingIncorrectUserData() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "wrongEmailTest", User.Role.ADMIN);
        MockHttpServletResponse response = mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userRequestDto.write(expected).getJson())
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Test authenticating user correctly")
    public void testAuthenticatingUserCorrectly() throws Exception {
        long id = 1L;
        UserAuthDtoResponse expected = new UserAuthDtoResponse(id, "email1@email.com", "test1_password", User.Role.ADMIN);
        doReturn(expected).when(userService).checkUserEmail(any(String.class));
        MockHttpServletResponse response = mvc.perform(get("/users/auth/email")).andReturn().getResponse();

        Type type = new TypeToken<UserAuthDtoResponse>() {
        }.getType();
        UserAuthDtoResponse userResponse = gson.fromJson(response.getContentAsString(), type);

        assertEquals(200, response.getStatus());
        assertEquals("test1_password", userResponse.getPassword());
        assertEquals("email1@email.com", userResponse.getEmail());
        assertEquals(id, userResponse.getId());
        assertEquals(User.Role.ADMIN, userResponse.getRole());
    }

    @Test
    @DisplayName("Test authenticating user incorrectly")
    public void testAuthenticatingUserIncorrectly() {
        assertThrows(NestedServletException.class, () -> mvc.perform(get("/users/auth/email")).andReturn().getResponse());
    }

    @Test
    @DisplayName("Test deleting user correctly")
    public void testDeletingUserCorrectly() throws Exception {
        long id = 1L;
        doReturn(true).when(userService).existsByUserId(id);
        MockHttpServletResponse response = mvc.perform(
                delete("/users/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
        ).andReturn().getResponse();

        assertEquals(204, response.getStatus());
        assertEquals("User with id 1 was successfully Deleted", response.getContentAsString());
    }

    @Test
    @DisplayName("Test deleting non-existing user")
    public void testDeletingNonExistingUser() throws Exception {
        doReturn(false).when(userService).existsByUserId(1);
        MockHttpServletResponse response = mvc.perform(
                delete("/users/-2")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjMsInN1YiI6ImFkbWluQGFkbWluLmNvbSJ9.vKLvrFh8L74Hu_24ZVQho1Bqbn35GfHmOMP6Ixiu870")
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
        assertEquals("{\"message\":\"No user was found;\"}", response.getContentAsString());
    }

    @Test
    @DisplayName("Test user deleting by unauthorized user")
    public void testUserDeletingByUnauthorizedUser() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                delete("/users/-2")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
        ).andReturn().getResponse();

        assertEquals(403, response.getStatus());
        assertEquals("{\"message\":\"You have no rights to perform such an action!\"}", response.getContentAsString());
    }

    @Test
    @DisplayName("Test updating user correctly")
    public void testUpdatingUserCorrectly() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "email1@email.com", User.Role.ADMIN);
        MockHttpServletResponse response = mvc.perform(
                put("/users/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userRequestDto.write(expected).getJson())
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("User with id 1 was successfully Updated", response.getContentAsString());
    }


    @Test
    @DisplayName("Test user updating by unauthorized user")
    public void testUserUpdatingByUnauthorizedUser() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "email1@email.com", User.Role.ADMIN);
        MockHttpServletResponse response = mvc.perform(
                put("/users/1000")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userRequestDto.write(expected).getJson())
        ).andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    @DisplayName("Test updating user with incorrect input data")
    public void testUpdatingUserWithIncorrectInput() throws Exception {
        long id = 1L;
        User expected = new User(id, "test1_username", "test1_password", "wrongEmail", User.Role.DEFAULT);
        MockHttpServletResponse response = mvc.perform(
                put("/users/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiREVGQVVMVCIsInVzZXJJZCI6MSwic3ViIjoidXNlckB1c2VyLmNvbSJ9.QbwFxO59kny5pICPwqCUujih_OSOXMwsET0IpHCD1sc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userRequestDto.write(expected).getJson())
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Test correct user privilege changing")
    public void testUserPrivilegeCorrectChanging() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                put("/users/privilege/1")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjMsInN1YiI6ImFkbWluQGFkbWluLmNvbSJ9.vKLvrFh8L74Hu_24ZVQho1Bqbn35GfHmOMP6Ixiu870")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }
}