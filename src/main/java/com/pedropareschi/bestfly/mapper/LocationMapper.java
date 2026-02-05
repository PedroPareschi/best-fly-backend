package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.LocationResponse;
import com.amadeus.resources.Location;

import java.util.*;
import java.util.stream.Collectors;

public class LocationMapper {

    public static List<LocationResponse> fromAmadeus(Location[] locations) {
        Map<String, List<Location>> groupedByCity = Arrays.stream(locations)
                .filter(location -> location.getAddress() != null)
                .collect(Collectors.groupingBy(location -> location.getAddress().getCityCode()));

        List<LocationResponse> locationResponses = new ArrayList<>();

        groupedByCity.forEach((cityCode, locationList) -> {
            String cityName = locationList.get(0).getAddress().getCityName();

            List<LocationResponse> subLocations = locationList.stream()
                    .map(location -> new LocationResponse(
                            location.getIataCode(),
                            location.getName(),
                            null))
                    .toList();

            locationResponses.add(new LocationResponse(
                    cityCode,
                    cityName,
                    subLocations
            ));
        });

        return locationResponses;
    }
}