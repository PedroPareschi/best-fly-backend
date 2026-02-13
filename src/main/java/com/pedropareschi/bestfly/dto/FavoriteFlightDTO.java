package com.pedropareschi.bestfly.dto;

import java.time.LocalDateTime;

public record FavoriteFlightDTO(
        Long id,
        Long userId,
        LocalDateTime createdAt,
        double price,
        String details
) {
}
