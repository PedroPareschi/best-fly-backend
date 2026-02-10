package com.pedropareschi.bestfly.controller;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.FlightDTO;
import com.pedropareschi.bestfly.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
public class FlightController {

    private FlightService flightService;

    @GetMapping
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam String originLocation,
            @RequestParam String destinationLocation,
            @RequestParam String departureDate,
            @RequestParam int numberOfAdults,
            @RequestParam(required = false) String returnDate,
            @RequestParam(defaultValue = "5") int max
    ) throws ResponseException {
        return ResponseEntity.ok(flightService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max));
    }
}