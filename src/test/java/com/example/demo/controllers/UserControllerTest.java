package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private CreateUserRequest createUserRequest;

    private String username;
    private String password;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        createUserRequest = new CreateUserRequest();
        username = "test" + System.currentTimeMillis();
        createUserRequest.setUsername(username);
        password = "test1234";
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(0l);

        when(encoder.encode(anyString())).thenReturn("This is hashed");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    }

    @Test
    public void createUserHappyPath() throws Exception {
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void findUserByName() throws Exception {
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        ResponseEntity<User> result = userController.findByUserName(username);
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        User resultUser = result.getBody();
        assertNotNull(resultUser);
        assertEquals(username, resultUser.getUsername());
    }

    @Test
    public void findUserById() throws Exception {
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        long userId = user.getId();
        ResponseEntity<User> result = userController.findById(userId);
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        User resultUser = result.getBody();
        assertNotNull(resultUser);
        assertEquals(username, resultUser.getUsername());
    }

    @Test
    public void createUserErrorMissingPassword() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        String username = "test" + System.currentTimeMillis();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword("");
        createUserRequest.setConfirmPassword("");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void createUserErrorDifferentPasswordAndConfirmPassword() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        String username = "test" + System.currentTimeMillis();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword("test1234");
        createUserRequest.setConfirmPassword("test4321");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void createUserErrorPasswordLengthLessThanEight() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        String username = "test" + System.currentTimeMillis();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword("test12");
        createUserRequest.setConfirmPassword("test12");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }
}
