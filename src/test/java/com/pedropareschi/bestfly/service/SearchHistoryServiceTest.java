package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.response.SearchHistoryResponse;
import com.pedropareschi.bestfly.entity.SearchHistory;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.SearchHistoryRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchHistoryServiceTest {

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchHistoryService searchHistoryService;

    @BeforeEach
    void setUpSecurity() {
        UserPrincipal principal = new UserPrincipal(1L, "user@email.com", "encoded");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listSearchHistoryShouldReturnMappedItems() {
        SearchHistory history = historyEntity(1L, 1L);
        when(searchHistoryRepository.findByUserId(1L)).thenReturn(List.of(history));

        List<SearchHistoryResponse> response = searchHistoryService.listSearchHistory();

        assertEquals(1, response.size());
        assertEquals("Sao Paulo", response.getFirst().originLocation());
        verify(searchHistoryRepository).findByUserId(1L);
    }

    @Test
    void getSearchHistoryShouldReturnEmptyWhenItemBelongsToAnotherUser() {
        when(searchHistoryRepository.findById(1L)).thenReturn(Optional.of(historyEntity(1L, 99L)));

        Optional<SearchHistoryResponse> response = searchHistoryService.getSearchHistory(1L);

        assertTrue(response.isEmpty());
    }

    @Test
    void createSearchHistoryShouldNotSaveWhenUserMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        searchHistoryService.createSearchHistory(sampleRequest());

        verify(searchHistoryRepository, never()).save(any(SearchHistory.class));
    }

    @Test
    void createSearchHistoryShouldSaveWhenUserExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(searchHistoryRepository.save(any(SearchHistory.class))).thenAnswer(invocation -> {
            SearchHistory saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        searchHistoryService.createSearchHistory(sampleRequest());

        ArgumentCaptor<SearchHistory> captor = ArgumentCaptor.forClass(SearchHistory.class);
        verify(searchHistoryRepository).save(captor.capture());
        SearchHistory saved = captor.getValue();
        assertEquals(1L, saved.getUser().getId());
        assertEquals("Sao Paulo", saved.getOriginLocation());
        assertEquals("New York", saved.getDestinationLocation());
        assertEquals(2, saved.getNumberOfAdults());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void deleteSearchHistoryShouldReturnFalseWhenItemMissing() {
        when(searchHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = searchHistoryService.deleteSearchHistory(1L);

        assertFalse(deleted);
        verify(searchHistoryRepository, never()).deleteById(1L);
    }

    @Test
    void deleteSearchHistoryShouldThrowForbiddenWhenItemBelongsToAnotherUser() {
        when(searchHistoryRepository.findById(1L)).thenReturn(Optional.of(historyEntity(1L, 99L)));

        assertThrows(ResponseStatusException.class, () -> searchHistoryService.deleteSearchHistory(1L));
    }

    @Test
    void deleteSearchHistoryShouldDeleteWhenItemBelongsToCurrentUser() {
        when(searchHistoryRepository.findById(1L)).thenReturn(Optional.of(historyEntity(1L, 1L)));

        boolean deleted = searchHistoryService.deleteSearchHistory(1L);

        assertTrue(deleted);
        verify(searchHistoryRepository).deleteById(1L);
    }

    private SearchHistory historyEntity(Long id, Long userId) {
        User user = new User();
        user.setId(userId);

        SearchHistory history = new SearchHistory();
        history.setId(id);
        history.setUser(user);
        history.setOriginLocation("Sao Paulo");
        history.setDestinationLocation("New York");
        history.setDepartureDate(LocalDateTime.now().plusDays(1));
        history.setNumberOfAdults(2);
        history.setReturnDate(LocalDateTime.now().plusDays(10));
        history.setCreatedAt(LocalDateTime.now());
        return history;
    }

    private CreateSearchHistoryRequest sampleRequest() {
        return new CreateSearchHistoryRequest(
                "offer-1",
                "Sao Paulo",
                "New York",
                LocalDateTime.now().plusDays(1),
                2,
                1,
                LocalDateTime.now().plusDays(10)
        );
    }
}
