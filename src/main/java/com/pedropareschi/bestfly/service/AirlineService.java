package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.AirlineDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirlineService {

    private ApiNinjasService apiNinjasService;

    public AirlineDTO getAirlineInfo(String airlineCode) throws ResponseException {
        return apiNinjasService.getAirlineByIata(airlineCode);
    }
}
