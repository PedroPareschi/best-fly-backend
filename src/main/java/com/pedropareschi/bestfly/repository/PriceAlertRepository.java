package com.pedropareschi.bestfly.repository;

import com.pedropareschi.bestfly.entity.PriceAlertSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlertSubscription, Long> {
    List<PriceAlertSubscription> findByUserId(Long userId);
    List<PriceAlertSubscription> findByActiveTrue();
}
