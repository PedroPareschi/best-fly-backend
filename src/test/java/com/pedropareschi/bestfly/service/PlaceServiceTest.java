package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import com.pedropareschi.bestfly.dto.response.PlaceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private DuffelService duffelService;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void searchPlacesShouldReturnMappedPlaces() {
        DuffelPlaceSuggestionsResponse.Airport gru = new DuffelPlaceSuggestionsResponse.Airport("GRU", "Guarulhos", "Sao Paulo");
        DuffelPlaceSuggestionsResponse.Place city = new DuffelPlaceSuggestionsResponse.Place(
                "city",
                "SAO",
                "Sao Paulo",
                "Sao Paulo",
                List.of(gru)
        );
        when(duffelService.listPlaceSuggestions("sao")).thenReturn(new DuffelPlaceSuggestionsResponse(List.of(city)));

        List<PlaceResponse> response = placeService.searchPlaces("sao");

        assertEquals(2, response.size());
        assertEquals("SAO", response.get(0).iataCode());
        assertEquals("GRU", response.get(1).iataCode());
    }

    @Test
    void searchPlacesShouldReturnEmptyWhenDuffelReturnsNull() {
        when(duffelService.listPlaceSuggestions("sao")).thenReturn(null);

        List<PlaceResponse> response = placeService.searchPlaces("sao");

        assertTrue(response.isEmpty());
    }
}
