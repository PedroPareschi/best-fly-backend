package com.pedropareschi.bestfly.dto.duffel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DuffelOfferListResponse(
        List<Offer> data,
        Meta meta
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Meta(
            String after,
            String before
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Offer(
            String id,
            String total_amount,
            String total_currency,
            List<Slice> slices
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Slice(
            String duration,
            Place origin,
            Place destination,
            List<Segment> segments
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Segment(
            String departing_at,
            String arriving_at,
            Place origin,
            Place destination,
            Carrier operating_carrier,
            Carrier marketing_carrier,
            List<Stop> stops
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Stop(
            String arrival_at,
            String departure_at,
            Place airport
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Place(
            String iata_code,
            String name,
            String city_name
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Carrier(
            String name,
            String iata_code,
            String logo_symbol_url,
            String conditions_of_carriage_url
    ) {
    }
}
