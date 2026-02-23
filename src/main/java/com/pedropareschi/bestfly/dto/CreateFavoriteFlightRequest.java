package com.pedropareschi.bestfly.dto;

public record CreateFavoriteFlightRequest(
        FlightSearchResponseDTO.DuffelFlightOfferDTO offer
) {
}
