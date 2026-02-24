package com.pedropareschi.bestfly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record PlaceDTO(
        String iataCode,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL) String cityName
) {
}
