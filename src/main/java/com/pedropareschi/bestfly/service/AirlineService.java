package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.AirlineInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirlineService {

    private AirlabsService airlabsService;

    public AirlineInfo getAirlineInfo(String airlineCode)  {
        return airlabsService.getAirlineByIata(airlineCode);
    }
}
