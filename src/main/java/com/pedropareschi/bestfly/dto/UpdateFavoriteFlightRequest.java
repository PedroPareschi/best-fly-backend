package com.pedropareschi.bestfly.dto;

public record UpdateFavoriteFlightRequest(
        FlightSearchResponseDTO.DuffelFlightOfferDTO offer
) {
}
