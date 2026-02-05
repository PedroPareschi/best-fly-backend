package com.pedropareschi.bestfly.dto.enums;

import com.amadeus.referenceData.Locations;
import lombok.Getter;

@Getter
public enum LocationSubType {
    ANY(Locations.ANY),
    AIRPORT(Locations.AIRPORT),
    CITY(Locations.CITY);

    private final String location;

    LocationSubType(String location) {
        this.location = location;
    }
}
