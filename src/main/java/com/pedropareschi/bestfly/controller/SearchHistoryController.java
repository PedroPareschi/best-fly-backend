package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.SearchHistoryDTO;
import com.pedropareschi.bestfly.dto.UpdateSearchHistoryRequest;
import com.pedropareschi.bestfly.service.SearchHistoryService;
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
@RequestMapping("/search-history")
@AllArgsConstructor
public class SearchHistoryController {

    private SearchHistoryService searchHistoryService;

    @GetMapping
    public ResponseEntity<List<SearchHistoryDTO>> listSearchHistory() {
        return ResponseEntity.ok(searchHistoryService.listSearchHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchHistoryDTO> getSearchHistory(@PathVariable Long id) {
        Optional<SearchHistoryDTO> history = searchHistoryService.getSearchHistory(id);
        return ResponseEntity.of(history);
    }

    @PostMapping
    public ResponseEntity<SearchHistoryDTO> createSearchHistory(@RequestBody CreateSearchHistoryRequest request) {
        Optional<SearchHistoryDTO> history = searchHistoryService.createSearchHistory(request);
        if (history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(history.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SearchHistoryDTO> updateSearchHistory(@PathVariable Long id, @RequestBody UpdateSearchHistoryRequest request) {
        Optional<SearchHistoryDTO> history = searchHistoryService.updateSearchHistory(id, request);
        return ResponseEntity.of(history);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchHistory(@PathVariable Long id) {
        if (!searchHistoryService.deleteSearchHistory(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
