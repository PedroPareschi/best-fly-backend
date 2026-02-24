package com.pedropareschi.bestfly.dto.request;

import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;

public record CreateFavoriteFlightRequest(
        FlightSearchResponse.DuffelFlightOfferDTO offer
) {
}
