package com.pedropareschi.bestfly.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email
) {
}
