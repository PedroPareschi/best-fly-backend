package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.DuffelFlightSearchResponseDTO;
import com.pedropareschi.bestfly.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
public class FlightController {

    private FlightService flightService;


    @GetMapping
    public ResponseEntity<DuffelFlightSearchResponseDTO> searchFlightsDuffel(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam(required = false) String departureTime,
            @RequestParam int numberOfAdults,
            @RequestParam(defaultValue = "0") int numberOfChildren,
            @RequestParam(required = false) String returnDate,
            @RequestParam(required = false) String returnTime,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String after
    ) {
        return ResponseEntity.ok(flightService.searchFlights(
                origin,
                destination,
                departureDate,
                departureTime,
                numberOfAdults,
                numberOfChildren,
                returnDate,
                returnTime,
                limit,
                after
        ));
    }
}
