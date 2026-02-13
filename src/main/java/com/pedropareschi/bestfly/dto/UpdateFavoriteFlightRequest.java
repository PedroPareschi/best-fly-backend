package com.pedropareschi.bestfly.dto;

public record UpdateFavoriteFlightRequest(
        Long userId,
        double price,
        String details
) {
}
