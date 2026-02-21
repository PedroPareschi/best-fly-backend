package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.DuffelFlightSearchResponseDTO;
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

    public DuffelFlightSearchResponseDTO searchFlights(
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
            return new DuffelFlightSearchResponseDTO(Collections.emptyList(), new DuffelFlightSearchResponseDTO.PaginationDTO(null, null, limit));
        }

        DuffelOfferListResponse offerListResponse = duffelService.listOffers(offerRequestId, limit, after);
        List<DuffelFlightSearchResponseDTO.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(offerListResponse);
        DuffelFlightSearchResponseDTO.PaginationDTO paginationDTO = FlightMapper.mapDuffelPagination(offerListResponse, limit);

        return new DuffelFlightSearchResponseDTO(offers, paginationDTO);
    }
}
