package com.pedropareschi.bestfly.dto;

import java.time.LocalDateTime;

public record SearchHistoryDTO(
        Long id,
        Long userId,
        String originLocation,
        String destinationLocation,
        LocalDateTime departureDate,
        int numberOfAdults,
        LocalDateTime returnDate,
        LocalDateTime createdAt
) {
}
