package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.PlaceDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlaceMapper {

    public static List<PlaceDTO> mapDuffelPlaceSuggestions(DuffelPlaceSuggestionsResponse response) {
        if (response == null || response.data() == null) {
            return List.of();
        }

        Map<String, PlaceDTO> unique = new LinkedHashMap<>();

        for (DuffelPlaceSuggestionsResponse.Place place : response.data()) {
            if (place == null) {
                continue;
            }

            if ("airport".equalsIgnoreCase(place.type())) {
                addPlace(unique, place.iata_code(), place.name(), place.city_name());
                continue;
            }

            if ("city".equalsIgnoreCase(place.type())) {
                addPlace(unique, place.iata_code(), place.name(), null);
                if (place.airports() == null) {
                    continue;
                }
                for (DuffelPlaceSuggestionsResponse.Airport airport : place.airports()) {
                    if (airport == null) {
                        continue;
                    }
                    String cityName = airport.city_name() != null ? airport.city_name() : place.name();
                    addPlace(unique, airport.iata_code(), airport.name(), cityName);
                }
            }
        }

        return new ArrayList<>(unique.values());
    }

    private static void addPlace(
            Map<String, PlaceDTO> unique,
            String iataCode,
            String airportName,
            String cityName
    ) {
        if (iataCode == null || iataCode.isBlank()) {
            return;
        }
        unique.putIfAbsent(iataCode, new PlaceDTO(iataCode, airportName, cityName));
    }
}
