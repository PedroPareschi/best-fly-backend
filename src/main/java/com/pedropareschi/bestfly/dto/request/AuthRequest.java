package com.pedropareschi.bestfly.dto.request;

public record AuthRequest(
        String email,
        String password
) {
}
