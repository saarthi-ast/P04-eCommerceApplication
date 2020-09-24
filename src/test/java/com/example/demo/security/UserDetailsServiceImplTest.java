package com.example.demo.security;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {
    private UserDetailsServiceImpl service;
    private UserRepository userRepository = mock(UserRepository.class);

    private CreateUserRequest createUserRequest;

    private String username;
    private String password;

    @Before
    public void setUp() {
        service = new UserDetailsServiceImpl();
        UserController userController = new UserController();
        TestUtils.injectObjects(service, "userRepository", userRepository);

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

        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void testLoadUserByName(){
        final UserDetails userDetails = service.loadUserByUsername("test");
        assertEquals(username,userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByNameFailure(){
        final UserDetails userDetails = service.loadUserByUsername("testwrong");
    }
}
