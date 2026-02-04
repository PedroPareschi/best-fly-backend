package com.pedropareschi.bestfly.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import com.pedropareschi.bestfly.mapper.AmadeusFlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AmadeusService {

    private final Amadeus amadeus;

    public Location[] searchLocations(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
            .with("keyword", keyword)
            .and("subType", Locations.AIRPORT));
    }

    public List<FlightSearchResponse> searchAmadeus(String origin, String destination, String departDate, int numberOfAdults, String returnDate) throws ResponseException {
        FlightOfferSearch[] amadeusSearchResult = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("returnDate", returnDate)
                        .and("adults", Integer.toString(numberOfAdults))
                        .and("max", 3));
        return  AmadeusFlightMapper.toFlightSearchResponse(amadeusSearchResult);
    }
}