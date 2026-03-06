package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.response.FavoriteFlightResponse;
import com.pedropareschi.bestfly.dto.request.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.service.FavoriteFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite-flights")
@AllArgsConstructor
@Tag(name = "Favorite Flights", description = "Management of favorite flights by the authenticated user")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteFlightController {

    private FavoriteFlightService favoriteFlightService;

    @GetMapping
    @Operation(summary = "List favorite flights", description = "Returns the favorite flights.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FavoriteFlightResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<List<FavoriteFlightResponse>> listFavoriteFlights() {
        return ResponseEntity.ok(favoriteFlightService.listFavoriteFlights());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get favorite flight by ID", description = "Returns a specific favorite flight.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Favorite flight found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteFlightResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Favorite flight not found", content = @Content)
    })
    public ResponseEntity<FavoriteFlightResponse> getFavoriteFlight(
            @Parameter(description = "Favorite flight ID", required = true) @PathVariable Long id
    ) {
        Optional<FavoriteFlightResponse> favorite = favoriteFlightService.getFavoriteFlight(id);
        return ResponseEntity.of(favorite);
    }

    @PostMapping
    @Operation(summary = "Create favorite flight", description = "Saves a new flight offer as favorite.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Favorite flight created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteFlightResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Related user not found", content = @Content)
    })
    public ResponseEntity<FavoriteFlightResponse> createFavoriteFlight(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Flight offer to be favorited", required = true)
            @RequestBody CreateFavoriteFlightRequest request
    ) {
        Optional<FavoriteFlightResponse> favorite = favoriteFlightService.createFavoriteFlight(request);
        if (favorite.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite.get());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update favorite flight", description = "Updates an existing favorite flight offer.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Favorite flight updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteFlightResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Favorite flight not found", content = @Content)
    })
    public ResponseEntity<FavoriteFlightResponse> updateFavoriteFlight(
            @Parameter(description = "Favorite flight ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated favorite offer data", required = true)
            @RequestBody UpdateFavoriteFlightRequest request
    ) {
        Optional<FavoriteFlightResponse> favorite = favoriteFlightService.updateFavoriteFlight(id, request);
        return ResponseEntity.of(favorite);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove favorite flight", description = "Removes a favorite flight by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Favorite flight removed successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Favorite flight not found", content = @Content)
    })
    public ResponseEntity<Void> deleteFavoriteFlight(
            @Parameter(description = "Favorite flight ID", required = true) @PathVariable Long id
    ) {
        if (!favoriteFlightService.deleteFavoriteFlight(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
