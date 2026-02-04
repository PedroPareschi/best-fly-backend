package com.pedropareschi.bestfly.dto;

public record FlightSearchResponse(
        String airlineName,
        int stops,
        String flightDuration,
        double price,
        String departureAirport,
        String arrivalAirport,
        String departureTime,
        String arrivalTime
) {
}