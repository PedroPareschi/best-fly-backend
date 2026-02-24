package com.pedropareschi.bestfly.dto.request;

import java.time.LocalDateTime;

public record UpdateSearchHistoryRequest(
        Long userId,
        String originLocation,
        String destinationLocation,
        LocalDateTime departureDate,
        int numberOfAdults,
        LocalDateTime returnDate
) {
}
