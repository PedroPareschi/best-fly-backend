package com.pedropareschi.bestfly.dto.request;

public record UpdateUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String city
) {
}
