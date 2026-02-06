package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.AirlineInfo;
import com.pedropareschi.bestfly.dto.FlightConfirmationResponse;
import com.pedropareschi.bestfly.dto.FlightInfo;
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
    private AirlineService airlineService;

    public FlightSearchResponse searchFlights(String originLocation, String destinationLocation, String departureDate, int numberOfAdults, String returnDate, int max) throws ResponseException {
        FlightSearchResponse flights = new FlightSearchResponse();

        Map<String, List<FlightInfo>> amadeusMap = FlightMapper.fromAmadeus(amadeusService.searchFlights(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate, max));
        flights.setDepartures(amadeusMap.get("departures"));
        flights.setReturns(amadeusMap.get("returns"));

        flights.getDepartures().sort(Comparator.comparingDouble(FlightInfo::price));
        flights.getReturns().sort(Comparator.comparingDouble(FlightInfo::price));

        return flights;
    }

    public FlightConfirmationResponse confirmFlight(FlightInfo departureFlight, FlightInfo returnFlight) throws ResponseException {
        AirlineInfo departureFlightAirline = airlineService.getAirlineInfo(departureFlight.airlineCode());
        AirlineInfo returnFlightAirline = null;
        if (returnFlight != null) {
            if (returnFlight.airlineCode().equals(departureFlight.airlineCode())) {
                returnFlightAirline = departureFlightAirline;
            } else {
                returnFlightAirline = airlineService.getAirlineInfo(returnFlight.airlineCode());
            }
        }
        double totalPrice = departureFlight.price() + returnFlight.price();
        FlightConfirmationResponse.FlightConfirmationInfo departureInfo = new FlightConfirmationResponse.FlightConfirmationInfo(departureFlight, departureFlightAirline);
        FlightConfirmationResponse.FlightConfirmationInfo returnInfo = new FlightConfirmationResponse.FlightConfirmationInfo(returnFlight, returnFlightAirline);
        return new FlightConfirmationResponse(departureInfo, returnInfo, totalPrice);
    }
}