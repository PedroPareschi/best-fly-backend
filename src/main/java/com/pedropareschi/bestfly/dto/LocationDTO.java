package com.pedropareschi.bestfly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record LocationDTO(
        String code,
        String name,
        String subType,
        @JsonInclude(JsonInclude.Include.NON_NULL) String city,
        String country
) {
}