package com.pedropareschi.bestfly.dto;

public record FlightConfirmationRequest (
        FlightInfo departureFlight,
        FlightInfo returnFlight
) {
}
