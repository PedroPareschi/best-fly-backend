package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.AirlineDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class AirlineService {

    private ApiNinjasService apiNinjasService;

    public Map<String, AirlineDTO> getAirlineInfo(Set<String> airlineCodes) {
        Map<String, AirlineDTO> airlineSet = new HashMap<>();
        for (String airlineCode : airlineCodes) {
            AirlineDTO airlineDTO = apiNinjasService.getAirlineByIata(airlineCode);
            airlineSet.put(airlineCode, airlineDTO);
        }
        return airlineSet;
    }
}
