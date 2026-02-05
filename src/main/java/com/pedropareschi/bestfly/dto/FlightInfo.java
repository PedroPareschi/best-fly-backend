package com.pedropareschi.bestfly.dto;

public record FlightInfo(
        String airlineCode,
        int stops,
        String flightDuration,
        double price,
        String departureAirport,
        String arrivalAirport,
        String departureTime,
        String arrivalTime
) {
}