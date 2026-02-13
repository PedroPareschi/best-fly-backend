package com.pedropareschi.bestfly.repository;

import com.pedropareschi.bestfly.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserId(Long userId);
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
