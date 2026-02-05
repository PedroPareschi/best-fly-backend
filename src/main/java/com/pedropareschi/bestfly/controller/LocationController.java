package com.pedropareschi.bestfly.controller;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.LocationResponse;
import com.pedropareschi.bestfly.dto.enums.LocationSubType;
import com.pedropareschi.bestfly.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class LocationController {

    private LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationResponse>> searchLocations(@RequestParam String keyword, @RequestParam LocationSubType subType) throws ResponseException {
        return ResponseEntity.ok(locationService.searchLocations(keyword, subType));
    }
}
