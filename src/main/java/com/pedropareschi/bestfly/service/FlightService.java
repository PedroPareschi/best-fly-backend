package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private AmadeusService amadeusService;

    public Location[] searchLocations(String keyword) throws ResponseException {
        return amadeusService.searchLocations(keyword);
    }

    public Page<FlightSearchResponse> searchFlights(String originLocation, String destinationLocation, String departureDate, int numberOfAdults, String returnDate, Pageable pageable) throws ResponseException {
        List<FlightSearchResponse> flights = new ArrayList<>();

        flights.addAll(amadeusService.searchAmadeus(originLocation, destinationLocation, departureDate, numberOfAdults, returnDate));

        flights.sort(Comparator.comparingDouble(FlightSearchResponse::price));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), flights.size());
        return new PageImpl<>(flights.subList(start, end), pageable, flights.size());
    }
}