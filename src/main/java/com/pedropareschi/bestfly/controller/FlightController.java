package com.pedropareschi.bestfly.controller;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.FlightConfirmationRequest;
import com.pedropareschi.bestfly.dto.FlightConfirmationResponse;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import com.pedropareschi.bestfly.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/flights")
@AllArgsConstructor
public class FlightController {

    private FlightService flightService;

    @GetMapping("/")
    public ResponseEntity<FlightSearchResponse> searchFlights(
            @RequestParam String originLocation,
            @RequestParam String destinationLocation,
            @RequestParam String departureDate,
            @RequestParam int numberOfAdults,
            @RequestParam(required = false) String returnDate,
            @RequestParam(defaultValue = "5") int max
    ) throws ResponseException {
        return ResponseEntity.ok(flightService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max));
    }

    @PostMapping("/confirmation")
    public ResponseEntity<FlightConfirmationResponse> confirmFlight(@RequestBody FlightConfirmationRequest request) {
        FlightConfirmationResponse response = flightService.confirmFlight(request.departureFlight(), request.returnFlight());
        return ResponseEntity.ok(response);
    }
}