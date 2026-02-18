package com.pedropareschi.bestfly.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email
) {
}
