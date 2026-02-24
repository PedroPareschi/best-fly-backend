package com.pedropareschi.bestfly.dto.response;

import java.time.LocalDateTime;

public record SearchHistoryResponse(
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
