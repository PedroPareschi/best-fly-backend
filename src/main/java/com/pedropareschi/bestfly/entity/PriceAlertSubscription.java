package com.pedropareschi.bestfly.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class PriceAlertSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private boolean active = true;
    private String originLocation;
    private String destinationLocation;
    private LocalDate departureDate;
    private String departureTime;
    private LocalDate returnDate;
    private String returnTime;
    private int numberOfAdults;
    private int numberOfChildren;
    private BigDecimal targetPriceAmount;
    private String targetPriceCurrency;
    private BigDecimal lastLowestPriceAmount;
    private String lastLowestPriceCurrency;
    private BigDecimal lastNotifiedPriceAmount;
    private String lastNotifiedPriceCurrency;
    private LocalDateTime lastCheckedAt;
    private LocalDateTime lastNotifiedAt;
    private LocalDateTime createdAt;
}
