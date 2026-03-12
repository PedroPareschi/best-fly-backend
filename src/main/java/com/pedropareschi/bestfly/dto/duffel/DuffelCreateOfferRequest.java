package com.pedropareschi.bestfly.dto.duffel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DuffelCreateOfferRequest(
        Data data
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            List<Slice> slices,
            List<Passenger> passengers
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Slice(
            String origin,
            String destination,
            String departure_date
    ) {
    }

    public record Passenger(
            String type
    ) {
    }
}
