package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SearchHistoryCleanupService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Value("${history.retentionDays:90}")
    private long retentionDays;

    public SearchHistoryCleanupService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Scheduled(cron = "${history.cleanupCron:0 0 3 * * *}")
    @Transactional
    public void cleanupOldHistory() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        searchHistoryRepository.deleteByCreatedAtBefore(cutoff);
    }
}
