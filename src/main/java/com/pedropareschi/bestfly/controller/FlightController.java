package com.pedropareschi.bestfly.controller;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import com.pedropareschi.bestfly.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FlightController {

    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<Page<FlightSearchResponse>> searchFlights(
            @RequestParam String originLocation,
            @RequestParam String destinationLocation,
            @RequestParam String departureDate,
            @RequestParam int numberOfAdults,
            @RequestParam(required = false) String returnDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws ResponseException {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(flightService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, pageRequest));
    }
}