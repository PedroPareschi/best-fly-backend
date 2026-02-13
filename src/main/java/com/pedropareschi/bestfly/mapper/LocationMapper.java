package com.pedropareschi.bestfly.mapper;

import com.amadeus.referenceData.Locations;
import com.pedropareschi.bestfly.dto.LocationDTO;
import com.amadeus.resources.Location;

import java.util.*;

public class LocationMapper {

    public static List<LocationDTO> fromAmadeus(Location[] locations) {
        return Arrays.stream(locations)
                .filter(location -> location.getAddress() != null)
                .map(location -> new LocationDTO(
                        location.getIataCode(),
                        location.getName(),
                        location.getSubType(),
                        Objects.equals(location.getSubType(), Locations.CITY) ? null : location.getAddress().getCityName(),
                        location.getAddress().getCountryName()
                ))
                .distinct()
                .toList();
    }
}