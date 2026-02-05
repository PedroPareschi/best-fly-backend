package com.pedropareschi.bestfly.dto;

import java.util.List;

public record AirlabsAirlineResponse(
        List<AirlineInfo> response
) {
}