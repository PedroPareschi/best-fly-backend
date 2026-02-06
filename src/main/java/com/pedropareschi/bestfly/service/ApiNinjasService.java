package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.AirlineInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ApiNinjasService {

    private final RestClient restClient;
    private final String apiKey;

    public ApiNinjasService(
            RestClient restClient,
            @Value("${apininjas.apiKey}") String apiKey
    ) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public AirlineInfo getAirlineByIata(String iataCode) {
        List<AirlineInfo> responseList =
                restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("api.api-ninjas.com")
                                .path("/v1/airlines")
                                .queryParam("iata", iataCode)
                                .build())
                        .header("x-api-key", apiKey)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {
                              }
                        );

        if (responseList == null || responseList.isEmpty()) {
            return null;
        }
        return responseList.getFirst();
    }
}