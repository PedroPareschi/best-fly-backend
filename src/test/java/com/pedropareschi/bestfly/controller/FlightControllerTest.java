package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import com.pedropareschi.bestfly.service.FlightService;
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
class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @Test
    void searchFlightsDuffelShouldDelegateToServiceAndReturnOk() {
        FlightSearchResponse serviceResponse = new FlightSearchResponse(
                List.of(),
                new FlightSearchResponse.PaginationDTO("after", "before", 20)
        );
        when(flightService.searchFlights("GRU", "JFK", "2026-01-01", null, 1, 0, null, null, 20, null))
                .thenReturn(serviceResponse);

        ResponseEntity<FlightSearchResponse> response = flightController.searchFlightsDuffel(
                "GRU", "JFK", "2026-01-01", null, 1, 0, null, null, 20, null
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceResponse, response.getBody());
        verify(flightService).searchFlights("GRU", "JFK", "2026-01-01", null, 1, 0, null, null, 20, null);
    }
}
