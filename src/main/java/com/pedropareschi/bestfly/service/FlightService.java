package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.FlightDTO;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private AmadeusService amadeusService;
    private AirlineService airlineService;

    public List<FlightDTO> searchFlights(String originLocation, String destinationLocation, String departureDate, int numberOfAdults, String returnDate, int max) throws ResponseException {
        FlightOfferSearch[] searchResponse = amadeusService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max);
        return FlightMapper.toFlightSearchResponse(searchResponse);
    }
}