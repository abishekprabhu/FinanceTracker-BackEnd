package com.abishek.financeapi.Controller;

import com.abishek.financeapi.DTO.UserDTO;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Service.User.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {


    // Register a new user successfully and return a 201 status
    @Test
    public void test_register_user_success() {
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com", "1234567890", "password", "password");
        User createdUser = new User(1L, "John Doe", "john.doe@example.com", "1234567890", "password", null, null, null, null, null);

        UserService userService = mock(UserService.class);
        when(userService.registerUser(userDTO)).thenReturn(createdUser);

        UserController userController = new UserController(userService);
        ResponseEntity<?> response = userController.registerUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
    }

    // Attempt to register with an existing email and return a 400 status
    @Test
    public void test_register_user_existing_email() {
        UserDTO userDTO = new UserDTO("Jane Doe", "jane.doe@example.com", "0987654321", "password", "password");

        UserService userService = mock(UserService.class);
        when(userService.registerUser(userDTO)).thenReturn(null);

        UserController userController = new UserController(userService);
        ResponseEntity<?> response = userController.registerUser(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists.", response.getBody());
    }

    // Login with valid credentials returns HTTP 200 and user data
    @Test
    public void test_login_with_valid_credentials() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        UserDTO userDTO = new UserDTO("John Doe", "john.doe@example.com", "1234567890", "password", "password");
        User user = new User(1L, "John Doe", "john.doe@example.com", "1234567890", "password", null, null, null, null, null);

        when(userService.loginUser(userDTO)).thenReturn(user);

        ResponseEntity<?> response = userController.LoginUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}