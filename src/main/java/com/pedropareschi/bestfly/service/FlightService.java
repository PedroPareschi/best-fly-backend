package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.FlightSearchResponseDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.mapper.FlightMapper;
import com.pedropareschi.bestfly.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private DuffelService duffelService;
    private SearchHistoryService searchHistoryService;

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

        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null) {
            LocalDateTime departureDateTime = toLocalDateTime(departureDate, departureTime);
            LocalDateTime returnDateTime = toLocalDateTime(returnDate, returnTime);
            CreateSearchHistoryRequest searchHistoryRequest = new CreateSearchHistoryRequest(
                    currentUserId,
                    offerRequestId,
                    origin,
                    destination,
                    departureDateTime,
                    numberOfAdults,
                    numberOfChildren,
                    returnDateTime
            );
            searchHistoryService.createSearchHistory(searchHistoryRequest);
        }

        return new FlightSearchResponseDTO(offers, paginationDTO);
    }

    private static LocalDateTime toLocalDateTime(String date, String time) {
        if (date == null || date.isBlank()) {
            return null;
        }
        LocalDate parsedDate = LocalDate.parse(date);
        if (time == null || time.isBlank()) {
            return parsedDate.atStartOfDay();
        }
        LocalTime parsedTime = LocalTime.parse(time);
        return LocalDateTime.of(parsedDate, parsedTime);
    }
}
