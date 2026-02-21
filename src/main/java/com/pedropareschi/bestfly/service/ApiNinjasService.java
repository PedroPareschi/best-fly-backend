package com.pedropareschi.bestfly.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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


}