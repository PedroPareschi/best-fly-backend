package com.pedropareschi.bestfly.dto;

import java.util.List;

public record DuffelFlightSearchResponseDTO(
        List<DuffelFlightOfferDTO> offers,
        PaginationDTO pagination
) {

    public record PaginationDTO(
            String after,
            String before,
            Integer limit
    ) {
    }

    public record DuffelFlightOfferDTO(
            PriceDTO price,
            SliceDTO outbound,
            SliceDTO inbound,
            AirlineDTO airline
    ) {
    }

    public record PriceDTO(
            String amount,
            String currency
    ) {
    }

    public record SliceDTO(
            String origin,
            String destination,
            String departureAt,
            String arrivalAt,
            String duration,
            int stopsCount,
            List<StopDTO> stops
    ) {
    }

    public record StopDTO(
            String airportCode,
            String name,
            String city
    ) {
    }
}
