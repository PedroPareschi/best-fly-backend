package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.PlaceDTO;
import com.pedropareschi.bestfly.service.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
@AllArgsConstructor
public class PlaceController {

    private PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces(@RequestParam String query) {
        return ResponseEntity.ok(placeService.searchPlaces(query));
    }
}
