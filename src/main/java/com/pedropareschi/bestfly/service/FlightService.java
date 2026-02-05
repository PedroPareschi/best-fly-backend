package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.FlightInfoResponse;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FlightService {

    private AmadeusService amadeusService;

    public FlightSearchResponse searchFlights(String originLocation, String destinationLocation, String departureDate, int numberOfAdults, String returnDate, int max) throws ResponseException {
        FlightSearchResponse flights = new FlightSearchResponse();

        Map<String, List<FlightInfoResponse>> amadeusMap = FlightMapper.fromAmadeus(amadeusService.searchAmadeus(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max));
        flights.setDepartures(amadeusMap.get("departures"));
        flights.setReturns(amadeusMap.get("returns"));

        flights.getDepartures().sort(Comparator.comparingDouble(FlightInfoResponse::price));
        flights.getReturns().sort(Comparator.comparingDouble(FlightInfoResponse::price));

        return flights;
    }
}