package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.UpdateUserRequest;
import com.pedropareschi.bestfly.dto.response.UserResponse;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUpSecurity() {
        UserPrincipal principal = new UserPrincipal(1L, "user@email.com", "encoded");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listUsersShouldReturnCurrentUserOnly() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity(1L, "user@email.com")));

        List<UserResponse> response = userService.listUsers();

        assertEquals(1, response.size());
        assertEquals(1L, response.getFirst().id());
        assertEquals("user@email.com", response.getFirst().email());
    }

    @Test
    void listUsersShouldReturnEmptyWhenCurrentUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        List<UserResponse> response = userService.listUsers();

        assertTrue(response.isEmpty());
    }

    @Test
    void getUserShouldThrowForbiddenWhenAccessingAnotherUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getUser(2L));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void updateUserShouldThrowConflictWhenEmailAlreadyRegistered() {
        User existing = userEntity(1L, "old@email.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("taken@email.com")).thenReturn(true);

        UpdateUserRequest request = new UpdateUserRequest("taken@email.com", null, "New", "Name", null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(1L, request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserShouldEncodePasswordAndPersistChanges() {
        User existing = userEntity(1L, "old@email.com");
        existing.setPassword("old-password");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("new@email.com")).thenReturn(false);
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateUserRequest request = new UpdateUserRequest(
                "new@email.com",
                "new-password",
                "New",
                "Name",
                null
        );

        Optional<UserResponse> response = userService.updateUser(1L, request);

        assertTrue(response.isPresent());
        assertEquals("new@email.com", response.get().email());
        assertEquals("New", response.get().firstName());
        assertEquals("Name", response.get().lastName());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("encoded-new-password", captor.getValue().getPassword());
    }

    @Test
    void deleteUserShouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean deleted = userService.deleteUser(1L);

        assertFalse(deleted);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void deleteUserShouldDeleteWhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean deleted = userService.deleteUser(1L);

        assertTrue(deleted);
        verify(userRepository).deleteById(1L);
    }

    private User userEntity(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName("User");
        user.setLastName("Test");
        return user;
    }
}
