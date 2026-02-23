package com.pedropareschi.bestfly.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class FavoriteFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime createdAt;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String airlineName;
    private String airlineIata;
    private String airlineLogoUrl;
    private String outboundOriginAirportCode;
    private String outboundOriginCity;
    private String outboundDestinationAirportCode;
    private String outboundDestinationCity;
    private String outboundDepartureAt;
    private String outboundArrivalAt;
    private String outboundDuration;
    private int outboundStopsCount;
    private String inboundDepartureAt;
    private String inboundArrivalAt;
    private String inboundDuration;
    private Integer inboundStopsCount;
}
