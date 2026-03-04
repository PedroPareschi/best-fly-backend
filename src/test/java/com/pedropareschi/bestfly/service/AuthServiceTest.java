package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.AuthRequest;
import com.pedropareschi.bestfly.dto.request.RegisterRequest;
import com.pedropareschi.bestfly.dto.response.AuthResponse;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.JwtService;
import com.pedropareschi.bestfly.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldThrowConflictWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("user@email.com", "123456", "User", "Test");
        when(userRepository.existsByEmail("user@email.com")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void registerShouldSaveUserAndReturnToken() {
        RegisterRequest request = new RegisterRequest("user@email.com", "123456", "User", "Test");
        when(userRepository.existsByEmail("user@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertEquals("user@email.com", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals("User", savedUser.getFirstName());
        assertEquals("Test", savedUser.getLastName());
        assertEquals("jwt-token", response.token());
        assertEquals("Bearer", response.tokenType());
        assertEquals(10L, response.userId());
        assertEquals("user@email.com", response.email());
    }

    @Test
    void loginShouldAuthenticateAndReturnToken() {
        AuthRequest request = new AuthRequest("user@email.com", "123456");
        UserPrincipal principal = new UserPrincipal(7L, "user@email.com", "encoded");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(principal)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.token());
        assertEquals("Bearer", response.tokenType());
        assertEquals(7L, response.userId());
        assertEquals("user@email.com", response.email());
    }
}
