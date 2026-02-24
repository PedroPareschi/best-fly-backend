package com.pedropareschi.bestfly.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FavoriteFlightResponse(
        Long id,
        Long userId,
        LocalDateTime createdAt,
        BigDecimal priceAmount,
        String priceCurrency,
        String airlineName,
        String airlineIata,
        String airlineLogoUrl,
        String outboundOriginAirportCode,
        String outboundOriginCity,
        String outboundDestinationAirportCode,
        String outboundDestinationCity,
        String outboundDepartureAt,
        String outboundArrivalAt,
        String outboundDuration,
        int outboundStopsCount,
        String inboundDepartureAt,
        String inboundArrivalAt,
        String inboundDuration,
        Integer inboundStopsCount
) {
}
