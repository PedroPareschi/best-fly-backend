package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.duffel.DuffelCreateOfferRequest;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class DuffelService {

    private final RestClient duffelRestClient;

    public DuffelService(@Qualifier("duffelRestClient") RestClient duffelRestClient) {
        this.duffelRestClient = duffelRestClient;
    }

    public DuffelOfferRequestResponse createOfferRequest(
            String origin,
            String destination,
            String departureDate,
            String departureTime,
            int numberOfAdults,
            int numberOfChildren,
            String returnDate,
            String returnTime
    ) {
        List<DuffelCreateOfferRequest.Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfAdults; i++) {
            passengers.add(new DuffelCreateOfferRequest.Passenger("adult"));
        }
        for (int i = 0; i < numberOfChildren; i++) {
            passengers.add(new DuffelCreateOfferRequest.Passenger("child"));
        }

        List<DuffelCreateOfferRequest.Slice> slices = new ArrayList<>();
        slices.add(buildSlice(origin, destination, departureDate, departureTime));

        if (returnDate != null && !returnDate.isBlank()) {
            slices.add(buildSlice(destination, origin, returnDate, returnTime));
        }

        DuffelCreateOfferRequest payload = new DuffelCreateOfferRequest(
                new DuffelCreateOfferRequest.Data(slices, passengers)
        );

        return duffelRestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/air/offer_requests")
                        .queryParam("return_offers", false)
                        .build())
                .body(payload)
                .retrieve()
                .body(DuffelOfferRequestResponse.class);
    }

    public DuffelOfferListResponse listOffers(String offerRequestId, int limit, String after) {
        return listOffers(offerRequestId, limit, after, null);
    }

    public DuffelOfferListResponse listOffers(String offerRequestId, int limit, String after, String sort) {
        return duffelRestClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/air/offers")
                            .queryParam("offer_request_id", offerRequestId)
                            .queryParam("limit", limit);
                    if (after != null && !after.isBlank()) {
                        builder.queryParam("after", after);
                    }
                    if (sort != null && !sort.isBlank()) {
                        builder.queryParam("sort", sort);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(DuffelOfferListResponse.class);
    }

    public DuffelPlaceSuggestionsResponse listPlaceSuggestions(String query) {
        return duffelRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/places/suggestions")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .body(DuffelPlaceSuggestionsResponse.class);
    }

    private DuffelCreateOfferRequest.Slice buildSlice(String origin, String destination, String date, String time) {
        DuffelCreateOfferRequest.DepartureTime departureTime = null;
        if (time != null && !time.isBlank()) {
            departureTime = new DuffelCreateOfferRequest.DepartureTime(time, time);
        }
        return new DuffelCreateOfferRequest.Slice(origin, destination, date, departureTime);
    }
}
