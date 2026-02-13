package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateUserRequest;
import com.pedropareschi.bestfly.dto.UpdateUserRequest;
import com.pedropareschi.bestfly.dto.UserDTO;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<UserDTO> listUsers() {
        return userRepository.findAll().stream()
                .map(UserService::toDTO)
                .toList();
    }

    public Optional<UserDTO> getUser(Long id) {
        return userRepository.findById(id).map(UserService::toDTO);
    }

    public UserDTO createUser(CreateUserRequest request) {
        User user = new User();
        applyCreateRequest(user, request);
        return toDTO(userRepository.save(user));
    }

    public Optional<UserDTO> updateUser(Long id, UpdateUserRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        applyUpdateRequest(user, request);
        return Optional.of(toDTO(userRepository.save(user)));
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private static void applyCreateRequest(User user, CreateUserRequest request) {
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setCity(request.city());
    }

    private static void applyUpdateRequest(User user, UpdateUserRequest request) {
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(request.password());
        }
        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }
        if (request.city() != null) {
            user.setCity(request.city());
        }
    }

    private static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCity()
        );
    }
}
