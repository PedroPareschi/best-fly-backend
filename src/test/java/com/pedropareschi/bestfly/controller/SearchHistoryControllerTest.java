package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.response.SearchHistoryResponse;
import com.pedropareschi.bestfly.service.SearchHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchHistoryControllerTest {

    @Mock
    private SearchHistoryService searchHistoryService;

    @InjectMocks
    private SearchHistoryController searchHistoryController;

    @Test
    void listSearchHistoryShouldReturnOk() {
        List<SearchHistoryResponse> history = List.of(sampleResponse(1L));
        when(searchHistoryService.listSearchHistory()).thenReturn(history);

        ResponseEntity<List<SearchHistoryResponse>> response = searchHistoryController.listSearchHistory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(history, response.getBody());
    }

    @Test
    void getSearchHistoryShouldReturnOkWhenFound() {
        SearchHistoryResponse item = sampleResponse(1L);
        when(searchHistoryService.getSearchHistory(1L)).thenReturn(Optional.of(item));

        ResponseEntity<SearchHistoryResponse> response = searchHistoryController.getSearchHistory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    void getSearchHistoryShouldReturnNotFoundWhenMissing() {
        when(searchHistoryService.getSearchHistory(1L)).thenReturn(Optional.empty());

        ResponseEntity<SearchHistoryResponse> response = searchHistoryController.getSearchHistory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createSearchHistoryShouldReturnCreated() {
        CreateSearchHistoryRequest request = new CreateSearchHistoryRequest(
                "offer",
                "Sao Paulo",
                "New York",
                LocalDateTime.now(),
                1,
                0,
                null
        );

        ResponseEntity<Void> response = searchHistoryController.createSearchHistory(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(searchHistoryService).createSearchHistory(request);
    }

    @Test
    void deleteSearchHistoryShouldReturnNoContentWhenDeleted() {
        when(searchHistoryService.deleteSearchHistory(1L)).thenReturn(true);

        ResponseEntity<Void> response = searchHistoryController.deleteSearchHistory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteSearchHistoryShouldReturnNotFoundWhenMissing() {
        when(searchHistoryService.deleteSearchHistory(1L)).thenReturn(false);

        ResponseEntity<Void> response = searchHistoryController.deleteSearchHistory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private SearchHistoryResponse sampleResponse(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return new SearchHistoryResponse(id, 1L, "Sao Paulo", "New York", now.plusDays(1), 1, now.plusDays(10), now);
    }
}
