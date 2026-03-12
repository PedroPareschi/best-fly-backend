package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private DuffelService duffelService;

    @InjectMocks
    private FlightService flightService;

    @Test
    void searchFlightsShouldReturnEmptyWhenOfferRequestIdIsMissing() {
        when(duffelService.createOfferRequest("GRU", "JFK", "2026-01-01", 1, 0, null))
                .thenReturn(new DuffelOfferRequestResponse(null));

        FlightSearchResponse response = flightService.searchFlights("GRU", "JFK", "2026-01-01", 1, 0, null, 20, null);

        assertEquals(0, response.offers().size());
        assertEquals(20, response.pagination().limit());
        assertNull(response.pagination().after());
        assertNull(response.pagination().before());
        verify(duffelService, never()).listOffers(anyString(), anyInt(), nullable(String.class));
    }

    @Test
    void searchFlightsShouldMapOffersAndPagination() {
        when(duffelService.createOfferRequest("GRU", "JFK", "2026-01-01", 1, 0, null))
                .thenReturn(new DuffelOfferRequestResponse(new DuffelOfferRequestResponse.Data("offer-req-1")));

        DuffelOfferListResponse.Place gru = new DuffelOfferListResponse.Place("GRU", "Guarulhos", "Sao Paulo");
        DuffelOfferListResponse.Place jfk = new DuffelOfferListResponse.Place("JFK", "John F Kennedy", "New York");

        DuffelOfferListResponse.Carrier carrier = new DuffelOfferListResponse.Carrier("Airline", "AA", "logo", "terms");
        DuffelOfferListResponse.Segment segment = new DuffelOfferListResponse.Segment(
                "2026-01-01T10:00:00",
                "2026-01-01T18:00:00",
                gru,
                jfk,
                carrier,
                null,
                List.of()
        );
        DuffelOfferListResponse.Slice outbound = new DuffelOfferListResponse.Slice("PT8H", gru, jfk, List.of(segment));

        DuffelOfferListResponse offerListResponse = new DuffelOfferListResponse(
                List.of(new DuffelOfferListResponse.Offer("offer-1", "123.45", "USD", List.of(outbound))),
                new DuffelOfferListResponse.Meta("cursor-after", "cursor-before")
        );

        when(duffelService.listOffers("offer-req-1", 20, null)).thenReturn(offerListResponse);

        FlightSearchResponse response = flightService.searchFlights("GRU", "JFK", "2026-01-01", 1, 0, null, 20, null);

        assertEquals(1, response.offers().size());
        assertEquals("123.45", response.offers().getFirst().price().amount());
        assertEquals("AA", response.offers().getFirst().airline().iata());
        assertEquals("cursor-after", response.pagination().after());
        assertEquals("cursor-before", response.pagination().before());
        assertEquals(20, response.pagination().limit());
    }
}
