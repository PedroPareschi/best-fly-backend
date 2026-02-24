package com.pedropareschi.bestfly.dto;

public record UpdateFavoriteFlightRequest(
        FlightSearchResponse.DuffelFlightOfferDTO offer
) {
}
