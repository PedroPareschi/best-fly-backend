package com.pedropareschi.bestfly.dto;

public record CreateFavoriteFlightRequest(
        FlightSearchResponse.DuffelFlightOfferDTO offer
) {
}
