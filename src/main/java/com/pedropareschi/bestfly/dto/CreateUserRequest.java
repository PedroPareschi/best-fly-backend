package com.pedropareschi.bestfly.dto;

public record CreateUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String city
) {
}
