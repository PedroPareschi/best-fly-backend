package com.pedropareschi.bestfly.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PriceAlertResponse(
        Long id,
        Long userId,
        boolean active,
        String originLocation,
        String destinationLocation,
        LocalDate departureDate,
        String departureTime,
        LocalDate returnDate,
        String returnTime,
        int numberOfAdults,
        int numberOfChildren,
        BigDecimal targetPriceAmount,
        String targetPriceCurrency,
        BigDecimal lastLowestPriceAmount,
        String lastLowestPriceCurrency,
        BigDecimal lastNotifiedPriceAmount,
        String lastNotifiedPriceCurrency,
        LocalDateTime lastCheckedAt,
        LocalDateTime lastNotifiedAt,
        LocalDateTime createdAt
) {
}
