package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import com.pedropareschi.bestfly.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
@Tag(name = "Flights", description = "Flight offers search")
public class FlightController {

    private FlightService flightService;


    @GetMapping
    @Operation(summary = "Search flights", description = "Search flight offers with filters for origin, destination, dates, and passengers.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Offers found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightSearchResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters", content = @Content)
    })
    public ResponseEntity<FlightSearchResponse> searchFlightsDuffel(
            @Parameter(description = "IATA code of origin", required = true, example = "GRU")
            @RequestParam String origin,
            @Parameter(description = "IATA code of destination", required = true, example = "JFK")
            @RequestParam String destination,
            @Parameter(description = "Departure date in YYYY-MM-DD format", required = true, example = "2026-04-10")
            @RequestParam String departureDate,
            @Parameter(description = "Number of adults", required = true, example = "1")
            @RequestParam int numberOfAdults,
            @Parameter(description = "Number of children", example = "0")
            @RequestParam(defaultValue = "0") int numberOfChildren,
            @Parameter(description = "Return date in YYYY-MM-DD format (optional)", example = "2026-04-20")
            @RequestParam(required = false) String returnDate,
            @Parameter(description = "Results limit", example = "20")
            @RequestParam(defaultValue = "20") int limit,
            @Parameter(description = "Pagination cursor (optional)")
            @RequestParam(required = false) String after
    ) {
        return ResponseEntity.ok(flightService.searchFlights(
                origin,
                destination,
                departureDate,
                numberOfAdults,
                numberOfChildren,
                returnDate,
                limit,
                after
        ));
    }
}
