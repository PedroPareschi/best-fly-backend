package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.response.PlaceResponse;
import com.pedropareschi.bestfly.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Places", description = "Search for airports and cities for origin/destination")
public class PlaceController {

    private PlaceService placeService;

    @GetMapping
    @Operation(summary = "Search places", description = "Searches places based on the provided term.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Places found",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PlaceResponse.class)))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid search parameter", content = @Content)
    })
    public ResponseEntity<List<PlaceResponse>> getPlaces(
            @Parameter(description = "Text for places search", required = true, example = "sao paulo")
            @RequestParam String query
    ) {
        return ResponseEntity.ok(placeService.searchPlaces(query));
    }
}
