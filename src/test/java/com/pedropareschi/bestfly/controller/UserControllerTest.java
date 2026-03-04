package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.UpdateUserRequest;
import com.pedropareschi.bestfly.dto.response.UserResponse;
import com.pedropareschi.bestfly.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void listUsersShouldReturnOk() {
        List<UserResponse> users = List.of(new UserResponse(1L, "user@email.com", "Pedro", "Pareschi"));
        when(userService.listUsers()).thenReturn(users);

        ResponseEntity<List<UserResponse>> response = userController.listUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getUserShouldReturnOkWhenFound() {
        UserResponse user = new UserResponse(1L, "user@email.com", "Pedro", "Pareschi");
        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserShouldReturnNotFoundWhenMissing() {
        when(userService.getUser(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userController.getUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateUserShouldReturnOkWhenUpdated() {
        UpdateUserRequest request = new UpdateUserRequest("user@email.com", null, "Pedro", "Pareschi", null);
        UserResponse user = new UserResponse(1L, "user@email.com", "Pedro", "Pareschi");
        when(userService.updateUser(1L, request)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userController.updateUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void updateUserShouldReturnNotFoundWhenMissing() {
        UpdateUserRequest request = new UpdateUserRequest("user@email.com", null, "Pedro", "Pareschi", null);
        when(userService.updateUser(1L, request)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userController.updateUser(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUserShouldReturnNoContentWhenDeleted() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUserShouldReturnNotFoundWhenMissing() {
        when(userService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
