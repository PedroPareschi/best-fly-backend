package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import com.pedropareschi.bestfly.dto.response.PlaceResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceMapperTest {

    @Test
    void mapDuffelPlaceSuggestionsShouldReturnEmptyWhenResponseIsNull() {
        List<PlaceResponse> places = PlaceMapper.mapDuffelPlaceSuggestions(null);

        assertTrue(places.isEmpty());
    }

    @Test
    void mapDuffelPlaceSuggestionsShouldMapAirportAndCityWithAirports() {
        DuffelPlaceSuggestionsResponse.Place airport = new DuffelPlaceSuggestionsResponse.Place(
                "airport",
                "JFK",
                "John F Kennedy",
                "New York",
                null
        );

        DuffelPlaceSuggestionsResponse.Airport gru = new DuffelPlaceSuggestionsResponse.Airport("GRU", "Guarulhos", "Sao Paulo");
        DuffelPlaceSuggestionsResponse.Airport cgh = new DuffelPlaceSuggestionsResponse.Airport("CGH", "Congonhas", null);
        DuffelPlaceSuggestionsResponse.Place city = new DuffelPlaceSuggestionsResponse.Place(
                "city",
                "SAO",
                "Sao Paulo",
                "Sao Paulo",
                List.of(gru, cgh)
        );

        List<PlaceResponse> places = PlaceMapper.mapDuffelPlaceSuggestions(
                new DuffelPlaceSuggestionsResponse(List.of(airport, city))
        );

        assertEquals(4, places.size());
        assertEquals("JFK", places.get(0).iataCode());
        assertEquals("SAO", places.get(1).iataCode());
        assertEquals("GRU", places.get(2).iataCode());
        assertEquals("CGH", places.get(3).iataCode());
        assertEquals("Sao Paulo", places.get(3).cityName());
    }

    @Test
    void mapDuffelPlaceSuggestionsShouldDeduplicateAndIgnoreInvalidIata() {
        DuffelPlaceSuggestionsResponse.Place duplicateAirport = new DuffelPlaceSuggestionsResponse.Place(
                "airport",
                "GRU",
                "Guarulhos A",
                "Sao Paulo",
                null
        );
        DuffelPlaceSuggestionsResponse.Place duplicateAirportSecond = new DuffelPlaceSuggestionsResponse.Place(
                "airport",
                "GRU",
                "Guarulhos B",
                "Sao Paulo",
                null
        );
        DuffelPlaceSuggestionsResponse.Place blankIata = new DuffelPlaceSuggestionsResponse.Place(
                "airport",
                "  ",
                "Ignored",
                "City",
                null
        );

        List<PlaceResponse> places = PlaceMapper.mapDuffelPlaceSuggestions(
                new DuffelPlaceSuggestionsResponse(Arrays.asList(null, duplicateAirport, duplicateAirportSecond, blankIata))
        );

        assertEquals(1, places.size());
        assertEquals("GRU", places.getFirst().iataCode());
        assertEquals("Guarulhos A", places.getFirst().name());
    }
}
