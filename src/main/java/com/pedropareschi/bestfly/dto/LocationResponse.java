package com.pedropareschi.bestfly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record LocationResponse(
        String code,
        String name,
        String subType,
        @JsonInclude(JsonInclude.Include.NON_NULL) String city,
        String country
) {
}