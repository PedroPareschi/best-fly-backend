package com.pedropareschi.bestfly.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.FlightInfoResponse;

import java.util.*;

public class FlightMapper {

    public static Map<String, List<FlightInfoResponse>> fromAmadeus(FlightOfferSearch[] flightOffers) {
        if (flightOffers == null) {
            return Collections.emptyMap();
        }

        List<FlightInfoResponse> departures = new ArrayList<>();
        List<FlightInfoResponse> returns = new ArrayList<>();

        for (FlightOfferSearch offer : flightOffers) {
            String airlineCode = offer.getValidatingAirlineCodes() != null &&
                    offer.getValidatingAirlineCodes().length > 0 ?
                    offer.getValidatingAirlineCodes()[0] : "Unknown";

            double price = offer.getPrice() != null ?
                    offer.getPrice().getTotal() : 0.0;

            String departureAirport = "Unknown";
            String arrivalAirport = "Unknown";
            String departureTime = "Unknown";
            String arrivalTime = "Unknown";
            String flightDuration = "00h 00m";
            int stops = 0;

            if (offer.getItineraries() != null && offer.getItineraries().length > 0) {
                departureAirport = offer.getItineraries()[0].getSegments() != null &&
                        offer.getItineraries()[0].getSegments().length > 0 ?
                        offer.getItineraries()[0].getSegments()[0].getDeparture().getIataCode() : "Unknown";

                arrivalAirport = offer.getItineraries()[0].getSegments() != null &&
                        offer.getItineraries()[0].getSegments().length > 0 ?
                        offer.getItineraries()[0].getSegments()[offer.getItineraries()[0].getSegments().length - 1].getArrival().getIataCode() : "Unknown";

                departureTime = offer.getItineraries()[0].getSegments() != null &&
                        offer.getItineraries()[0].getSegments().length > 0 ?
                        offer.getItineraries()[0].getSegments()[0].getDeparture().getAt() : "Unknown";

                arrivalTime = offer.getItineraries()[0].getSegments() != null &&
                        offer.getItineraries()[0].getSegments().length > 0 ?
                        offer.getItineraries()[0].getSegments()[offer.getItineraries()[0].getSegments().length - 1].getArrival().getAt() : "Unknown";

                flightDuration = offer.getItineraries()[0].getDuration() != null ?
                        offer.getItineraries()[0].getDuration() : "00h 00m";

                stops = offer.getItineraries()[0].getSegments() != null ?
                        offer.getItineraries()[0].getSegments().length - 1 : 0;
            }

            if (offer.getItineraries() != null && offer.getItineraries().length > 1) {
                String returnDepartureAirport = offer.getItineraries()[1].getSegments() != null &&
                        offer.getItineraries()[1].getSegments().length > 0 ?
                        offer.getItineraries()[1].getSegments()[0].getDeparture().getIataCode() : "Unknown";

                String returnArrivalAirport = offer.getItineraries()[1].getSegments() != null &&
                        offer.getItineraries()[1].getSegments().length > 0 ?
                        offer.getItineraries()[1].getSegments()[offer.getItineraries()[1].getSegments().length - 1].getArrival().getIataCode() : "Unknown";

                String returnDepartureTime = offer.getItineraries()[1].getSegments() != null &&
                        offer.getItineraries()[1].getSegments().length > 0 ?
                        offer.getItineraries()[1].getSegments()[0].getDeparture().getAt() : "Unknown";

                String returnArrivalTime = offer.getItineraries()[1].getSegments() != null &&
                        offer.getItineraries()[1].getSegments().length > 0 ?
                        offer.getItineraries()[1].getSegments()[offer.getItineraries()[1].getSegments().length - 1].getArrival().getAt() : "Unknown";

                String returnDuration = offer.getItineraries()[1].getDuration() != null ?
                        offer.getItineraries()[1].getDuration() : "00h 00m";

                int returnStops = offer.getItineraries()[1].getSegments() != null ?
                        offer.getItineraries()[1].getSegments().length - 1 : 0;

                returns.add(new FlightInfoResponse(
                        airlineCode,
                        returnStops,
                        returnDuration,
                        price,
                        returnDepartureAirport,
                        returnArrivalAirport,
                        returnDepartureTime,
                        returnArrivalTime
                ));
            }

            departures.add(new FlightInfoResponse(
                    airlineCode,
                    stops,
                    flightDuration,
                    price,
                    departureAirport,
                    arrivalAirport,
                    departureTime,
                    arrivalTime
            ));
        }

        Map<String, List<FlightInfoResponse>> flights = new HashMap<>();
        flights.put("departures", departures);
        flights.put("returns", returns);
        return flights;
    }
}