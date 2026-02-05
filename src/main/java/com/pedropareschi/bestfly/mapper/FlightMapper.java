package com.pedropareschi.bestfly.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;

import java.util.ArrayList;
import java.util.List;

public class FlightMapper {

    public static List<FlightSearchResponse> fromAmadeus(FlightOfferSearch[] flightOffers) {
        List<FlightSearchResponse> responses = new ArrayList<>();

        if (flightOffers == null) {
            return responses;
        }

        for (FlightOfferSearch offer : flightOffers) {
            String airlineName = offer.getValidatingAirlineCodes() != null &&
                    offer.getValidatingAirlineCodes().length > 0 ?
                    offer.getValidatingAirlineCodes()[0] : "Unknown";

            int stops = offer.getItineraries() != null &&
                    offer.getItineraries().length > 0 ?
                    offer.getItineraries()[0].getSegments().length - 1 : 0;

            String flightDuration = offer.getItineraries() != null &&
                    offer.getItineraries().length > 0 ?
                    offer.getItineraries()[0].getDuration() : "00h 00m";

            double price = offer.getPrice() != null ?
                    offer.getPrice().getTotal() : 0.0;

            String departureAirport = offer.getItineraries() != null &&
                    offer.getItineraries()[0].getSegments() != null &&
                    offer.getItineraries()[0].getSegments().length > 0 ?
                    offer.getItineraries()[0].getSegments()[0].getDeparture().getIataCode() : "Unknown";

            String arrivalAirport = offer.getItineraries() != null &&
                    offer.getItineraries()[0].getSegments() != null &&
                    offer.getItineraries()[0].getSegments().length > 0 ?
                    offer.getItineraries()[0].getSegments()[offer.getItineraries()[0].getSegments().length - 1].getArrival().getIataCode() : "Unknown";

            String departureTime = offer.getItineraries() != null &&
                    offer.getItineraries()[0].getSegments() != null &&
                    offer.getItineraries()[0].getSegments().length > 0 ?
                    offer.getItineraries()[0].getSegments()[0].getDeparture().getAt() : "Unknown";

            String arrivalTime = offer.getItineraries() != null &&
                    offer.getItineraries()[0].getSegments() != null &&
                    offer.getItineraries()[0].getSegments().length > 0 ?
                    offer.getItineraries()[0].getSegments()[offer.getItineraries()[0].getSegments().length - 1].getArrival().getAt() : "Unknown";

            responses.add(new FlightSearchResponse(
                    airlineName,
                    stops,
                    flightDuration,
                    price,
                    departureAirport,
                    arrivalAirport,
                    departureTime,
                    arrivalTime
            ));
        }

        return responses;
    }
}