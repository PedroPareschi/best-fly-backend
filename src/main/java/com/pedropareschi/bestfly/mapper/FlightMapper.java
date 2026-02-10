package com.pedropareschi.bestfly.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.pedropareschi.bestfly.dto.FlightDTO;

import java.util.*;

public class FlightMapper {

    public static List<FlightDTO> toFlightSearchResponse(FlightOfferSearch[] flightOffers) {
        if (flightOffers == null) {
            return null;
        }

        List<FlightDTO> flights = new ArrayList<>();

        for (FlightOfferSearch offer : flightOffers) {

            String airlineCode = (offer.getValidatingAirlineCodes() != null &&
                    offer.getValidatingAirlineCodes().length > 0)
                    ? offer.getValidatingAirlineCodes()[0]
                    : "Unknown";

            FlightDTO.FlightPriceDTO priceDTO =
                    new FlightDTO.FlightPriceDTO(
                            offer.getPrice().getGrandTotal(),
                            offer.getPrice().getCurrency()
                    );

            FlightDTO.FlightPricingDTO pricingDTO = extractPricing(offer);

            List<FlightDTO.ItineraryDTO> itineraries = mapItineraries(offer);

            FlightDTO dto = new FlightDTO(
                    airlineCode,
                    offer.getNumberOfBookableSeats(),
                    priceDTO,
                    pricingDTO,
                    itineraries
            );

            flights.add(dto);
        }

        return flights;
    }

    private static List<FlightDTO.ItineraryDTO> mapItineraries(FlightOfferSearch offer) {
        List<FlightDTO.ItineraryDTO> itineraries = new ArrayList<>();

        if (offer.getItineraries() == null) return itineraries;

        for (FlightOfferSearch.Itinerary itinerary : offer.getItineraries()) {

            List<FlightDTO.SegmentDTO> segments = new ArrayList<>();

            if (itinerary.getSegments() != null) {
                for (FlightOfferSearch.SearchSegment segment : itinerary.getSegments()) {

                    FlightDTO.AirportDTO departure =
                            new FlightDTO.AirportDTO(
                                    segment.getDeparture().getIataCode(),
                                    segment.getDeparture().getAt(),
                                    segment.getDeparture().getTerminal()
                            );

                    FlightDTO.AirportDTO arrival =
                            new FlightDTO.AirportDTO(
                                    segment.getArrival().getIataCode(),
                                    segment.getArrival().getAt(),
                                    segment.getArrival().getTerminal()
                            );

                    FlightDTO.SegmentDTO segmentDTO =
                            new FlightDTO.SegmentDTO(
                                    segment.getCarrierCode() + segment.getNumber(),
                                    segment.getAircraft() != null ? segment.getAircraft().getCode() : "Unknown",
                                    departure,
                                    arrival
                            );

                    segments.add(segmentDTO);
                }
            }

            itineraries.add(new FlightDTO.ItineraryDTO(
                    itinerary.getDuration(),
                    segments
            ));
        }

        return itineraries;
    }

    private static FlightDTO.FlightPricingDTO extractPricing(FlightOfferSearch offer) {

        boolean refundableFare = false;
        boolean noRestrictionFare = false;
        boolean noPenaltyFare = false;
        boolean includedCheckedBagsOnly = false;

        if (offer.getTravelerPricings() != null &&
                offer.getTravelerPricings().length > 0 &&
                offer.getTravelerPricings()[0].getFareDetailsBySegment() != null &&
                offer.getTravelerPricings()[0].getFareDetailsBySegment().length > 0) {

            FlightOfferSearch.PricingOptions pricingOptions = offer.getPricingOptions();

            refundableFare = pricingOptions.isRefundableFare();
            noRestrictionFare = pricingOptions.isNoRestrictionFare();
            noPenaltyFare = pricingOptions.isNoPenaltyFare();

            FlightOfferSearch.FareDetailsBySegment fare = offer.getTravelerPricings()[0].getFareDetailsBySegment()[0];

            if (pricingOptions.isIncludedCheckedBagsOnly()) {
                includedCheckedBagsOnly = fare.getIncludedCheckedBags().getWeight() > 0;
            }
        }

        return new FlightDTO.FlightPricingDTO(
                includedCheckedBagsOnly,
                refundableFare,
                noRestrictionFare,
                noPenaltyFare
        );
    }
}