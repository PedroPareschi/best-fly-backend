package com.pedropareschi.bestfly.dto.request;

public record CreateUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String city
) {
}
