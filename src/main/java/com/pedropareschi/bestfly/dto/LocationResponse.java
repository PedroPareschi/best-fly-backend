package com.pedropareschi.bestfly.dto;

import java.util.List;

public record LocationResponse(
        String code,
        String name,
        List<LocationResponse> subLocations
) {
}