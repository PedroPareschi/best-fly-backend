package com.pedropareschi.bestfly.dto;

import java.time.LocalDate;

public record UpdateSearchHistoryRequest(
        Long userId,
        String originLocation,
        String destinationLocation,
        LocalDate departureDate,
        int numberOfAdults,
        LocalDate returnDate
) {
}
