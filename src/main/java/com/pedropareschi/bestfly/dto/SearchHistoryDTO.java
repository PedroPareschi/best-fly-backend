package com.pedropareschi.bestfly.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SearchHistoryDTO(
        Long id,
        Long userId,
        String originLocation,
        String destinationLocation,
        LocalDate departureDate,
        int numberOfAdults,
        LocalDate returnDate,
        LocalDateTime createdAt
) {
}
