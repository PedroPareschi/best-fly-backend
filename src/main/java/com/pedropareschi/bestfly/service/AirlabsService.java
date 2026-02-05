package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.AirlabsAirlineResponse;
import com.pedropareschi.bestfly.dto.AirlineInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AirlabsService {

    private final RestClient restClient;
    private final String apiKey;

    public AirlabsService(
            RestClient restClient,
            @Value("${airlabs.apiKey}") String apiKey
    ) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public AirlineInfo getAirlineByIata(String iataCode) {

        AirlabsAirlineResponse response =
                restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("airlabs.co")
                                .path("/api/v9/airlines")
                                .queryParam("iata_code", iataCode)
                                .queryParam("api_key", apiKey)
                                .build())
                        .retrieve()
                        .body(AirlabsAirlineResponse.class);

        if (response == null || response.response().isEmpty()) {
            return null;
        }

        return response.response().getFirst();
    }
}