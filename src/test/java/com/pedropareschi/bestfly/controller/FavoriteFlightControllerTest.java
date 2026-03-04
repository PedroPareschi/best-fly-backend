package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.request.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.response.FavoriteFlightResponse;
import com.pedropareschi.bestfly.service.FavoriteFlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteFlightControllerTest {

    @Mock
    private FavoriteFlightService favoriteFlightService;

    @InjectMocks
    private FavoriteFlightController favoriteFlightController;

    @Test
    void listFavoriteFlightsShouldReturnOk() {
        List<FavoriteFlightResponse> favorites = List.of(sampleResponse(1L));
        when(favoriteFlightService.listFavoriteFlights()).thenReturn(favorites);

        ResponseEntity<List<FavoriteFlightResponse>> response = favoriteFlightController.listFavoriteFlights();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(favorites, response.getBody());
        verify(favoriteFlightService).listFavoriteFlights();
    }

    @Test
    void getFavoriteFlightShouldReturnOkWhenFound() {
        FavoriteFlightResponse favorite = sampleResponse(1L);
        when(favoriteFlightService.getFavoriteFlight(1L)).thenReturn(Optional.of(favorite));

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.getFavoriteFlight(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(favorite, response.getBody());
    }

    @Test
    void getFavoriteFlightShouldReturnNotFoundWhenMissing() {
        when(favoriteFlightService.getFavoriteFlight(1L)).thenReturn(Optional.empty());

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.getFavoriteFlight(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createFavoriteFlightShouldReturnCreatedWhenSaved() {
        CreateFavoriteFlightRequest request = new CreateFavoriteFlightRequest(null);
        FavoriteFlightResponse favorite = sampleResponse(1L);
        when(favoriteFlightService.createFavoriteFlight(request)).thenReturn(Optional.of(favorite));

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.createFavoriteFlight(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(favorite, response.getBody());
    }

    @Test
    void createFavoriteFlightShouldReturnNotFoundWhenUserMissing() {
        CreateFavoriteFlightRequest request = new CreateFavoriteFlightRequest(null);
        when(favoriteFlightService.createFavoriteFlight(request)).thenReturn(Optional.empty());

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.createFavoriteFlight(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateFavoriteFlightShouldReturnOkWhenFound() {
        UpdateFavoriteFlightRequest request = new UpdateFavoriteFlightRequest(null);
        FavoriteFlightResponse favorite = sampleResponse(1L);
        when(favoriteFlightService.updateFavoriteFlight(1L, request)).thenReturn(Optional.of(favorite));

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.updateFavoriteFlight(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(favorite, response.getBody());
    }

    @Test
    void updateFavoriteFlightShouldReturnNotFoundWhenMissing() {
        UpdateFavoriteFlightRequest request = new UpdateFavoriteFlightRequest(null);
        when(favoriteFlightService.updateFavoriteFlight(1L, request)).thenReturn(Optional.empty());

        ResponseEntity<FavoriteFlightResponse> response = favoriteFlightController.updateFavoriteFlight(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteFavoriteFlightShouldReturnNoContentWhenDeleted() {
        when(favoriteFlightService.deleteFavoriteFlight(1L)).thenReturn(true);

        ResponseEntity<Void> response = favoriteFlightController.deleteFavoriteFlight(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteFavoriteFlightShouldReturnNotFoundWhenMissing() {
        when(favoriteFlightService.deleteFavoriteFlight(1L)).thenReturn(false);

        ResponseEntity<Void> response = favoriteFlightController.deleteFavoriteFlight(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private FavoriteFlightResponse sampleResponse(Long id) {
        return new FavoriteFlightResponse(
                id,
                1L,
                LocalDateTime.now(),
                null,
                "USD",
                "Airline",
                "AA",
                "logo",
                "GRU",
                "Sao Paulo",
                "JFK",
                "New York",
                "2026-01-01T10:00:00",
                "2026-01-01T18:00:00",
                "PT8H",
                0,
                null,
                null,
                null,
                null
        );
    }
}
