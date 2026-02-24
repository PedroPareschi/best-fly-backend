package com.pedropareschi.bestfly.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record PlaceResponse(
        String iataCode,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL) String cityName
) {
}
