package com.pedropareschi.bestfly.dto;

import java.util.List;

public record FlightDTO(
        int numberOfBookableSeats,
        FlightPriceDTO price,
        AirlineDTO airline,
        FlightPricingDTO pricing,
        List<ItineraryDTO> itineraries
) {

    public record ItineraryDTO(
            String duration,
            List<SegmentDTO> segments
    ) {
        public int getStops() {
            return segments.size() - 1;
        }
    }

    public record SegmentDTO(
            String flightNumber,
            String aircraftCode,
            AirportDTO departure,
            AirportDTO arrival
    ) {
    }

    public record AirportDTO(
            String airportCode,
            String time,
            String terminal
    ) {
    }

    public record FlightPriceDTO(
            double total,
            String currency
    ) {
    }

    public record FlightPricingDTO(
            boolean includedCheckedBagsOnly,
            boolean refundableFare,
            boolean noRestrictionFare,
            boolean noPenaltyFare
    ) {
    }
}