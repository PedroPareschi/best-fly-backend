package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.FlightSearchResponseDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private DuffelService duffelService;

    public FlightSearchResponseDTO searchFlights(
            String origin,
            String destination,
            String departureDate,
            String departureTime,
            int numberOfAdults,
            int numberOfChildren,
            String returnDate,
            String returnTime,
            int limit,
            String after
    ) {
        DuffelOfferRequestResponse offerRequestResponse = duffelService.createOfferRequest(
                origin,
                destination,
                departureDate,
                departureTime,
                numberOfAdults,
                numberOfChildren,
                returnDate,
                returnTime
        );

        String offerRequestId = offerRequestResponse != null && offerRequestResponse.data() != null
                ? offerRequestResponse.data().id()
                : null;

        if (offerRequestId == null) {
            return new FlightSearchResponseDTO(Collections.emptyList(), new FlightSearchResponseDTO.PaginationDTO(null, null, limit));
        }

        DuffelOfferListResponse offerListResponse = duffelService.listOffers(offerRequestId, limit, after);
        List<FlightSearchResponseDTO.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(offerListResponse);
        FlightSearchResponseDTO.PaginationDTO paginationDTO = FlightMapper.mapDuffelPagination(offerListResponse, limit);

        return new FlightSearchResponseDTO(offers, paginationDTO);
    }
}
