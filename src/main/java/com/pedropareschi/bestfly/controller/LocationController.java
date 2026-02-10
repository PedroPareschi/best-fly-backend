package com.pedropareschi.bestfly.controller;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.LocationResponseDTO;
import com.pedropareschi.bestfly.dto.enums.LocationSubType;
import com.pedropareschi.bestfly.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController {

    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> searchLocations(@RequestParam String keyword, @RequestParam LocationSubType subType) throws ResponseException {
        return ResponseEntity.ok(locationService.searchLocations(keyword, subType));
    }
}
