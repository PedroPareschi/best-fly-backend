package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.config.CacheConfig;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private DuffelService duffelService;

    @Cacheable(
            cacheNames = CacheConfig.FLIGHT_SEARCH_CACHE,
            key = "{#origin,#destination,#departureDate,#departureTime,#numberOfAdults,#numberOfChildren,#returnDate,#returnTime,#limit,#after}"
    )
    public FlightSearchResponse searchFlights(
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
            return new FlightSearchResponse(Collections.emptyList(), new FlightSearchResponse.PaginationDTO(null, null, limit));
        }

        DuffelOfferListResponse offerListResponse = duffelService.listOffers(offerRequestId, limit, after);
        List<FlightSearchResponse.DuffelFlightOfferDTO> offers = FlightMapper.mapDuffelOffers(offerListResponse);
        FlightSearchResponse.PaginationDTO paginationDTO = FlightMapper.mapDuffelPagination(offerListResponse, limit);

        return new FlightSearchResponse(offers, paginationDTO);
    }
}
