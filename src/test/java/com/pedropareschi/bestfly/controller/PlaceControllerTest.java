package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.response.PlaceResponse;
import com.pedropareschi.bestfly.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private PlaceController placeController;

    @Test
    void getPlacesShouldReturnOk() {
        List<PlaceResponse> places = List.of(new PlaceResponse("GRU", "Guarulhos", "Sao Paulo"));
        when(placeService.searchPlaces("sao")).thenReturn(places);

        ResponseEntity<List<PlaceResponse>> response = placeController.getPlaces("sao");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(places, response.getBody());
        verify(placeService).searchPlaces("sao");
    }
}
