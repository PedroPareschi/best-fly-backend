package com.pedropareschi.bestfly.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.pedropareschi.bestfly.dto.enums.LocationSubType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AmadeusService {

    private final Amadeus amadeus;

    public Location[] searchLocations(String keyword, LocationSubType subType) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", subType.getLocation()));
    }

    public FlightOfferSearch[] searchFlights(String origin, String destination, String departDate, int numberOfAdults, String returnDate, int max) throws ResponseException {
        Params params = Params.with("originLocationCode", origin)
                .and("destinationLocationCode", destination)
                .and("departureDate", departDate)
                .and("adults", Integer.toString(numberOfAdults))
                .and("max", max);

        if (returnDate != null && !returnDate.isEmpty()) {
            params.and("returnDate", returnDate);
        }

        return amadeus.shopping.flightOffersSearch.get(params);
    }

}