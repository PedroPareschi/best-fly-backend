package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class DuffelServiceTest {

    private DuffelService duffelService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://duffel.test");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        duffelService = new DuffelService(builder.build());
    }

    @Test
    void createOfferRequestShouldBuildPassengersAndRoundTripSlices() {
        mockServer.expect(requestTo("https://duffel.test/air/offer_requests?return_offers=false"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.data.passengers.length()").value(3))
                .andExpect(jsonPath("$.data.passengers[0].type").value("adult"))
                .andExpect(jsonPath("$.data.passengers[2].type").value("child"))
                .andExpect(jsonPath("$.data.slices.length()").value(2))
                .andExpect(jsonPath("$.data.slices[0].origin").value("GRU"))
                .andExpect(jsonPath("$.data.slices[0].destination").value("JFK"))
                .andExpect(jsonPath("$.data.slices[1].origin").value("JFK"))
                .andExpect(jsonPath("$.data.slices[1].destination").value("GRU"))
                .andRespond(withSuccess("{\"data\":{\"id\":\"offer-req-1\"}}", MediaType.APPLICATION_JSON));

        DuffelOfferRequestResponse response = duffelService.createOfferRequest(
                "GRU",
                "JFK",
                "2026-01-01",
                2,
                1,
                "2026-01-10"
        );

        assertEquals("offer-req-1", response.data().id());
        mockServer.verify();
    }

    @Test
    void listOffersShouldIncludeAfterCursorWhenProvided() {
        mockServer.expect(requestTo("https://duffel.test/air/offers?offer_request_id=req-1&limit=20&after=cursor-1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"data\":[],\"meta\":{\"after\":\"next-cursor\",\"before\":\"prev-cursor\"}}",
                        MediaType.APPLICATION_JSON
                ));

        DuffelOfferListResponse response = duffelService.listOffers("req-1", 20, "cursor-1");

        assertEquals("next-cursor", response.meta().after());
        assertEquals("prev-cursor", response.meta().before());
        mockServer.verify();
    }

    @Test
    void listOffersShouldOmitAfterCursorWhenBlank() {
        mockServer.expect(requestTo("https://duffel.test/air/offers?offer_request_id=req-1&limit=20"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"data\":[],\"meta\":{\"after\":null,\"before\":null}}",
                        MediaType.APPLICATION_JSON
                ));

        DuffelOfferListResponse response = duffelService.listOffers("req-1", 20, "   ");

        assertEquals(0, response.data().size());
        mockServer.verify();
    }

    @Test
    void listPlaceSuggestionsShouldCallSuggestionsEndpoint() {
        mockServer.expect(requestTo("https://duffel.test/places/suggestions?query=GRU"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"data\":[{\"type\":\"city\",\"iata_code\":\"SAO\",\"name\":\"Sao Paulo\",\"city_name\":\"Sao Paulo\",\"airports\":[]}]}",
                        MediaType.APPLICATION_JSON
                ));

        DuffelPlaceSuggestionsResponse response = duffelService.listPlaceSuggestions("GRU");

        assertEquals(1, response.data().size());
        assertEquals("SAO", response.data().getFirst().iata_code());
        mockServer.verify();
    }
}
