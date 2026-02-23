package com.pedropareschi.bestfly.dto;

import java.time.LocalDateTime;

public record CreateSearchHistoryRequest(
        Long userId,
        String offerRequestId,
        String originLocation,
        String destinationLocation,
        LocalDateTime departureDate,
        int numberOfAdults,
        int numberOfChildren,
        LocalDateTime returnDate
) {
}
