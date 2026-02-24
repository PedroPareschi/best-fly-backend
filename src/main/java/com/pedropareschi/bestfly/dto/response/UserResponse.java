package com.pedropareschi.bestfly.dto.response;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
