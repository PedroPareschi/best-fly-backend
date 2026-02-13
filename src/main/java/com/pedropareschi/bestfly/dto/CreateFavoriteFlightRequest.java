package com.pedropareschi.bestfly.dto;

public record CreateFavoriteFlightRequest(
        Long userId,
        double price,
        String details
) {
}
