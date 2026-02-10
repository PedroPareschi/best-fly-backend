package com.pedropareschi.bestfly.dto;

import java.util.List;

public record FlightDTO(
        String airlineCode,
        int numberOfBookableSeats,
        FlightPriceDTO price,
        FlightPricingDTO pricing,
        List<ItineraryDTO> itineraries
) {

    public record ItineraryDTO(
            String duration,
            List<SegmentDTO> segments
    ) {}

    public record SegmentDTO(
            String flightNumber,
            String aircraftCode,
            int stops,
            AirportDTO departure,
            AirportDTO arrival
    ) {}

    public record AirportDTO(
            String airportCode,
            String time,
            String terminal
    ) {}

    public record FlightPriceDTO(
            double total,
            String currency
    ) {}

    public record FlightPricingDTO(
            boolean includedCheckedBagsOnly,
            boolean refundableFare,
            boolean noRestrictionFare,
            boolean noPenaltyFare
    ) {}
}