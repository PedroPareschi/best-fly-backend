package com.pedropareschi.bestfly.dto;

public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String city
) {
}
