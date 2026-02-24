package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.UpdateUserRequest;
import com.pedropareschi.bestfly.dto.response.UserResponse;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public List<UserResponse> listUsers() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userRepository.findById(currentUserId)
                .map(user -> List.of(toDTO(user)))
                .orElseGet(List::of);
    }

    public Optional<UserResponse> getUser(Long id) {
        requireSelf(id);
        return userRepository.findById(id).map(UserService::toDTO);
    }

    public Optional<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        requireSelf(id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        if (request.email() != null && !request.email().equals(user.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        applyUpdateRequest(user, request, passwordEncoder);
        return Optional.of(toDTO(userRepository.save(user)));
    }

    public boolean deleteUser(Long id) {
        requireSelf(id);
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private static void applyUpdateRequest(User user, UpdateUserRequest request, PasswordEncoder passwordEncoder) {
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }
    }

    private static UserResponse toDTO(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private static void requireSelf(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!currentUserId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }
}
