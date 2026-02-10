package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.AirlineDTO;
import com.pedropareschi.bestfly.dto.FlightDTO;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class FlightService {

    private AmadeusService amadeusService;
    private AirlineService airlineService;

    public List<FlightDTO> searchFlights(String originLocation, String destinationLocation, String departureDate, int numberOfAdults, String returnDate, int max) throws ResponseException {
        FlightOfferSearch[] searchResponse = amadeusService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max);
        Set<String> airlineCodesSet = new LinkedHashSet<>();
        for (FlightOfferSearch offer : searchResponse) {
            airlineCodesSet.add((offer.getValidatingAirlineCodes() != null &&
                    offer.getValidatingAirlineCodes().length > 0)
                    ? offer.getValidatingAirlineCodes()[0]
                    : "Unknown");
        }
        Map<String, AirlineDTO> airlines = airlineService.getAirlineInfo(airlineCodesSet);
        return FlightMapper.toFlightSearchResponse(searchResponse, airlines);
    }
}