package com.pedropareschi.bestfly.dto.duffel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DuffelPlaceSuggestionsResponse(
        List<Place> data
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Place(
            String type,
            String iata_code,
            String name,
            String city_name,
            List<Airport> airports
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Airport(
            String iata_code,
            String name,
            String city_name
    ) {
    }
}
