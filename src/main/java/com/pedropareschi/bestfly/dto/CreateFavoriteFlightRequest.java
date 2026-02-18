package com.pedropareschi.bestfly.dto;

public record CreateFavoriteFlightRequest(
        double price,
        String details
) {
}
