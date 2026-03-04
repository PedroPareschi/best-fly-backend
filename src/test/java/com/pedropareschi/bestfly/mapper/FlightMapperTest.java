package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightMapperTest {

    @Test
    void mapDuffelOffersShouldReturnEmptyWhenResponseIsNull() {
        List<FlightSearchResponse.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(null);

        assertTrue(offers.isEmpty());
    }

    @Test
    void mapDuffelOffersShouldSkipOffersWithoutSlices() {
        DuffelOfferListResponse offerListResponse = new DuffelOfferListResponse(
                List.of(
                        new DuffelOfferListResponse.Offer("offer-1", "100.00", "USD", null),
                        new DuffelOfferListResponse.Offer("offer-2", "200.00", "USD", List.of())
                ),
                null
        );

        List<FlightSearchResponse.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(offerListResponse);

        assertTrue(offers.isEmpty());
    }

    @Test
    void mapDuffelOffersShouldMapOutboundInboundStopsAndAirline() {
        DuffelOfferListResponse.Place gru = new DuffelOfferListResponse.Place("GRU", "Guarulhos", "Sao Paulo");
        DuffelOfferListResponse.Place lis = new DuffelOfferListResponse.Place("LIS", "Lisbon", "Lisbon");
        DuffelOfferListResponse.Place cdg = new DuffelOfferListResponse.Place("CDG", "Charles de Gaulle", "Paris");
        DuffelOfferListResponse.Place jfk = new DuffelOfferListResponse.Place("JFK", "John F Kennedy", "New York");

        DuffelOfferListResponse.Carrier marketingCarrier = new DuffelOfferListResponse.Carrier(
                "Marketing Airline", "MA", "logo-url", "terms-url"
        );

        DuffelOfferListResponse.Stop technicalStop = new DuffelOfferListResponse.Stop(
                "2026-01-01T13:00:00Z",
                "2026-01-01T14:00:00Z",
                lis
        );

        DuffelOfferListResponse.Segment firstSegment = new DuffelOfferListResponse.Segment(
                "2026-01-01T10:00:00Z",
                "2026-01-01T12:00:00Z",
                gru,
                cdg,
                null,
                marketingCarrier,
                List.of(technicalStop)
        );
        DuffelOfferListResponse.Segment secondSegment = new DuffelOfferListResponse.Segment(
                "2026-01-01T15:00:00Z",
                "2026-01-01T17:00:00Z",
                cdg,
                jfk,
                null,
                null,
                null
        );

        DuffelOfferListResponse.Slice outbound = new DuffelOfferListResponse.Slice(
                "PT10H", gru, jfk, List.of(firstSegment, secondSegment)
        );
        DuffelOfferListResponse.Slice inbound = new DuffelOfferListResponse.Slice(
                "PT9H", jfk, gru, List.of(secondSegment)
        );

        DuffelOfferListResponse response = new DuffelOfferListResponse(
                List.of(new DuffelOfferListResponse.Offer("offer-1", "123.45", "USD", List.of(outbound, inbound))),
                null
        );

        List<FlightSearchResponse.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(response);

        assertEquals(1, offers.size());
        FlightSearchResponse.DuffelFlightOfferDTO offer = offers.getFirst();

        assertEquals("123.45", offer.price().amount());
        assertEquals("USD", offer.price().currency());

        assertEquals("2026-01-01T10:00:00Z", offer.outbound().departureAt());
        assertEquals("2026-01-01T17:00:00Z", offer.outbound().arrivalAt());
        assertEquals(2, offer.outbound().stopsCount());
        assertEquals("LIS", offer.outbound().stops().get(0).place().airportCode());
        assertEquals("PT1H", offer.outbound().stops().get(0).waitDuration());
        assertEquals("CDG", offer.outbound().stops().get(1).place().airportCode());
        assertEquals("PT3H", offer.outbound().stops().get(1).waitDuration());

        assertEquals("MA", offer.airline().iata());
        assertEquals("logo-url", offer.airline().logoUrl());
        assertEquals("terms-url", offer.airline().conditionsOfCarriageUrl());

        assertEquals("PT9H", offer.inbound().duration());
    }

    @Test
    void mapDuffelOffersShouldSetNullWaitDurationWhenDatesAreInvalid() {
        DuffelOfferListResponse.Place gru = new DuffelOfferListResponse.Place("GRU", "Guarulhos", "Sao Paulo");
        DuffelOfferListResponse.Stop invalidStop = new DuffelOfferListResponse.Stop("invalid", "also-invalid", gru);
        DuffelOfferListResponse.Segment segment = new DuffelOfferListResponse.Segment(
                "2026-01-01T10:00:00",
                "2026-01-01T12:00:00",
                gru,
                gru,
                null,
                null,
                List.of(invalidStop)
        );

        DuffelOfferListResponse response = new DuffelOfferListResponse(
                List.of(new DuffelOfferListResponse.Offer("offer-1", "1", "USD", List.of(
                        new DuffelOfferListResponse.Slice("PT1H", gru, gru, List.of(segment))
                ))),
                null
        );

        FlightSearchResponse.DuffelFlightOfferDTO offer = FlightMapper.mapDuffelOffers(response).getFirst();

        assertNull(offer.outbound().stops().getFirst().waitDuration());
    }

    @Test
    void mapDuffelPaginationShouldMapMetaAndLimit() {
        DuffelOfferListResponse response = new DuffelOfferListResponse(
                List.of(),
                new DuffelOfferListResponse.Meta("after-cursor", "before-cursor")
        );

        FlightSearchResponse.PaginationDTO pagination = FlightMapper.mapDuffelPagination(response, 30);

        assertEquals("after-cursor", pagination.after());
        assertEquals("before-cursor", pagination.before());
        assertEquals(30, pagination.limit());
    }

    @Test
    void mapDuffelPaginationShouldHandleNullResponse() {
        FlightSearchResponse.PaginationDTO pagination = FlightMapper.mapDuffelPagination(null, 15);

        assertNull(pagination.after());
        assertNull(pagination.before());
        assertEquals(15, pagination.limit());
    }
}
