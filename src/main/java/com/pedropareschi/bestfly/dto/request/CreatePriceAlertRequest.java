package com.pedropareschi.bestfly.dto.request;

import java.math.BigDecimal;

public record CreatePriceAlertRequest(
        String originLocation,
        String destinationLocation,
        String departureDate,
        String departureTime,
        String returnDate,
        String returnTime,
        Integer numberOfAdults,
        Integer numberOfChildren,
        BigDecimal targetPriceAmount,
        String targetPriceCurrency
) {
}
