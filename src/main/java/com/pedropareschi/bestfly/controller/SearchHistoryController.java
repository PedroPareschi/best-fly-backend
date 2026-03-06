package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.response.SearchHistoryResponse;
import com.pedropareschi.bestfly.dto.request.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.service.SearchHistoryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search-history")
@AllArgsConstructor
@Tag(name = "Search History", description = "Management of the authenticated user's search history")
@SecurityRequirement(name = "bearerAuth")
public class SearchHistoryController {

    private SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "List search history", description = "Returns the user's search history.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "History returned successfully",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SearchHistoryResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<List<SearchHistoryResponse>> listSearchHistory() {
        return ResponseEntity.ok(searchHistoryService.listSearchHistory());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get search history item by ID", description = "Returns a specific search history item.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchHistoryResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Search history item not found", content = @Content)
    })
    public ResponseEntity<SearchHistoryResponse> getSearchHistory(
            @Parameter(description = "Search history item ID", required = true) @PathVariable Long id
    ) {
        Optional<SearchHistoryResponse> history = searchHistoryService.getSearchHistory(id);
        return ResponseEntity.of(history);
    }

    @PostMapping
    @Operation(summary = "Create search history item", description = "Records a new search in the history.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Search recorded successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data for history creation", content = @Content)
    })
    public ResponseEntity<Void> createSearchHistory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Performed search data", required = true)
            @RequestBody CreateSearchHistoryRequest request
    ) {
        searchHistoryService.createSearchHistory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove search history item", description = "Removes a search history item by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removed successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Search history item not found", content = @Content)
    })
    public ResponseEntity<Void> deleteSearchHistory(
            @Parameter(description = "Search history item ID", required = true) @PathVariable Long id
    ) {
        if (!searchHistoryService.deleteSearchHistory(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
