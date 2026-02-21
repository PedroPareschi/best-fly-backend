package com.pedropareschi.bestfly.dto.duffel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DuffelOfferRequestResponse(
        Data data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(
            String id
    ) {
    }
}
