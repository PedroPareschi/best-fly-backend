package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.AuthRequest;
import com.pedropareschi.bestfly.dto.AuthResponse;
import com.pedropareschi.bestfly.dto.RegisterRequest;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.JwtService;
import com.pedropareschi.bestfly.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setCity(request.city());
        User saved = userRepository.save(user);
        UserPrincipal principal = UserPrincipal.fromUser(saved);
        String token = jwtService.generateToken(principal);
        return new AuthResponse(token, "Bearer", saved.getId(), saved.getEmail());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        return new AuthResponse(token, "Bearer", principal.getId(), principal.getUsername());
    }
}
