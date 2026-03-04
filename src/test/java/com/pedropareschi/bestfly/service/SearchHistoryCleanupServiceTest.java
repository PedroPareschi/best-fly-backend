package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.repository.SearchHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchHistoryCleanupServiceTest {

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @InjectMocks
    private SearchHistoryCleanupService searchHistoryCleanupService;

    @Test
    void cleanupOldHistoryShouldDeleteEntriesBeforeConfiguredCutoff() {
        ReflectionTestUtils.setField(searchHistoryCleanupService, "retentionDays", 30L);
        LocalDateTime beforeCall = LocalDateTime.now();

        searchHistoryCleanupService.cleanupOldHistory();

        LocalDateTime afterCall = LocalDateTime.now();
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(searchHistoryRepository).deleteByCreatedAtBefore(captor.capture());

        LocalDateTime expectedMin = beforeCall.minusDays(30).minusSeconds(1);
        LocalDateTime expectedMax = afterCall.minusDays(30).plusSeconds(1);
        LocalDateTime actualCutoff = captor.getValue();

        assertTrue(!actualCutoff.isBefore(expectedMin) && !actualCutoff.isAfter(expectedMax));
    }
}
