package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.FavoriteFlightDTO;
import com.pedropareschi.bestfly.dto.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.service.FavoriteFlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite-flights")
@AllArgsConstructor
public class FavoriteFlightController {

    private FavoriteFlightService favoriteFlightService;

    @GetMapping
    public ResponseEntity<List<FavoriteFlightDTO>> listFavoriteFlights(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(favoriteFlightService.listFavoriteFlights(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteFlightDTO> getFavoriteFlight(@PathVariable Long id) {
        Optional<FavoriteFlightDTO> favorite = favoriteFlightService.getFavoriteFlight(id);
        return ResponseEntity.of(favorite);
    }

    @PostMapping
    public ResponseEntity<FavoriteFlightDTO> createFavoriteFlight(@RequestBody CreateFavoriteFlightRequest request) {
        Optional<FavoriteFlightDTO> favorite = favoriteFlightService.createFavoriteFlight(request);
        if (favorite.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteFlightDTO> updateFavoriteFlight(@PathVariable Long id, @RequestBody UpdateFavoriteFlightRequest request) {
        Optional<FavoriteFlightDTO> favorite = favoriteFlightService.updateFavoriteFlight(id, request);
        return ResponseEntity.of(favorite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteFlight(@PathVariable Long id) {
        if (!favoriteFlightService.deleteFavoriteFlight(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
