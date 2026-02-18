package com.pedropareschi.bestfly.dto;

public record AuthRequest(
        String email,
        String password
) {
}
