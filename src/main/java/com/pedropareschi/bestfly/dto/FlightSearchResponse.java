package com.pedropareschi.bestfly.dto;

import java.util.List;

public record FlightSearchResponse(
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

    public record AirlineDTO(
            String name,
            String iata,
            String logo_url
    ) {
    }

    public record PriceDTO(
            String amount,
            String currency
    ) {
    }

    public record SliceDTO(
            PlaceDTO origin,
            PlaceDTO destination,
            String departureAt,
            String arrivalAt,
            String duration,
            int stopsCount,
            List<StopDTO> stops
    ) {
    }

    public record PlaceDTO(
            String airportCode,
            String name,
            String city
    ) {
    }

    public record StopDTO(
            PlaceDTO place,
            String arrivalAt,
            String waitDuration,
            String departureAt
    ) {
    }

}
