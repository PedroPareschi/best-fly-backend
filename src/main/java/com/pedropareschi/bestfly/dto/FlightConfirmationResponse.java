package com.pedropareschi.bestfly.dto;

public record FlightConfirmationResponse(
        FlightConfirmationInfo departureFlight,
        FlightConfirmationInfo returnFlight,
        double totalPrice
) {
    public record FlightConfirmationInfo(
            FlightInfo flightInfo,
            AirlineInfo airlineInfo
    ) {
    }
}
