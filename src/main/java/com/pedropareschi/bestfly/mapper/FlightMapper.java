package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.FlightSearchResponseDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class FlightMapper {

    public static List<FlightSearchResponseDTO.DuffelFlightOfferDTO> mapDuffelOffers(DuffelOfferListResponse offerListResponse) {
        if (offerListResponse == null || offerListResponse.data() == null) {
            return Collections.emptyList();
        }

        List<FlightSearchResponseDTO.DuffelFlightOfferDTO> offers = new ArrayList<>();
        for (DuffelOfferListResponse.Offer offer : offerListResponse.data()) {
            List<DuffelOfferListResponse.Slice> slices = offer.slices();
            if (slices == null || slices.isEmpty()) {
                continue;
            }

            DuffelOfferListResponse.Slice outboundSlice = slices.getFirst();
            DuffelOfferListResponse.Slice inboundSlice = slices.size() > 1 ? slices.get(1) : null;

            FlightSearchResponseDTO.SliceDTO outbound = mapDuffelSlice(outboundSlice);
            FlightSearchResponseDTO.SliceDTO inbound = inboundSlice != null ? mapDuffelSlice(inboundSlice) : null;

            FlightSearchResponseDTO.AirlineDTO airline = resolveDuffelAirline(outboundSlice);

            FlightSearchResponseDTO.PriceDTO price = new FlightSearchResponseDTO.PriceDTO(
                    offer.total_amount(),
                    offer.total_currency()
            );

            offers.add(new FlightSearchResponseDTO.DuffelFlightOfferDTO(
                    price,
                    outbound,
                    inbound,
                    airline
            ));
        }

        return offers;
    }

    public static FlightSearchResponseDTO.PaginationDTO mapDuffelPagination(DuffelOfferListResponse offerListResponse, int limit) {
        return new FlightSearchResponseDTO.PaginationDTO(
                offerListResponse != null && offerListResponse.meta() != null ? offerListResponse.meta().after() : null,
                offerListResponse != null && offerListResponse.meta() != null ? offerListResponse.meta().before() : null,
                limit
        );
    }

    private static FlightSearchResponseDTO.SliceDTO mapDuffelSlice(DuffelOfferListResponse.Slice slice) {
        if (slice == null) {
            return null;
        }

        List<DuffelOfferListResponse.Segment> segments = slice.segments();
        String departureAt = null;
        String arrivalAt = null;
        if (segments != null && !segments.isEmpty()) {
            departureAt = segments.getFirst().departing_at();
            arrivalAt = segments.getLast().arriving_at();
        }

        List<FlightSearchResponseDTO.StopDTO> stops = mapDuffelStops(segments);

        return new FlightSearchResponseDTO.SliceDTO(
                mapDuffelPlace(slice.origin()),
                mapDuffelPlace(slice.destination()),
                departureAt,
                arrivalAt,
                slice.duration(),
                stops.size(),
                stops
        );
    }

    private static List<FlightSearchResponseDTO.StopDTO> mapDuffelStops(List<DuffelOfferListResponse.Segment> segments) {
        if (segments == null || segments.isEmpty()) {
            return Collections.emptyList();
        }

        List<FlightSearchResponseDTO.StopDTO> stops = new ArrayList<>();

        for (int i = 0; i < segments.size(); i++) {
            DuffelOfferListResponse.Segment segment = segments.get(i);

            if (i > 0) {
                DuffelOfferListResponse.Segment previousSegment = segments.get(i - 1);
                DuffelOfferListResponse.Place layoverPlace = segment.origin();
                addDuffelStopWithTimes(
                        stops,
                        layoverPlace,
                        previousSegment.arriving_at(),
                        segment.departing_at()
                );
            }

            if (segment.stops() == null) {
                continue;
            }
            for (DuffelOfferListResponse.Stop stop : segment.stops()) {
                addDuffelStopWithTimes(
                        stops,
                        stop.airport(),
                        stop.arrival_at(),
                        stop.departure_at()
                );
            }
        }

        return stops;
    }

    private static void addDuffelStopWithTimes(
            List<FlightSearchResponseDTO.StopDTO> stops,
            DuffelOfferListResponse.Place place,
            String arrivalAt,
            String departureAt
    ) {
        if (place == null || place.iata_code() == null) {
            return;
        }

        String waitDuration = calculateWaitDuration(arrivalAt, departureAt);
        FlightSearchResponseDTO.PlaceDTO placeDTO = mapDuffelPlace(place);
        stops.add(new FlightSearchResponseDTO.StopDTO(
                placeDTO,
                arrivalAt,
                waitDuration,
                departureAt
        ));
    }

    private static FlightSearchResponseDTO.PlaceDTO mapDuffelPlace(DuffelOfferListResponse.Place place) {
        if (place == null) {
            return null;
        }
        return new FlightSearchResponseDTO.PlaceDTO(
                place.iata_code(),
                place.name(),
                place.city_name()
        );
    }

    private static String calculateWaitDuration(String arrivalAt, String departureAt) {
        if (arrivalAt == null || departureAt == null) {
            return null;
        }

        try {
            OffsetDateTime arrival = OffsetDateTime.parse(arrivalAt);
            OffsetDateTime departure = OffsetDateTime.parse(departureAt);
            Duration duration = Duration.between(arrival, departure);
            return duration.toString();
        } catch (DateTimeParseException ex) {
            // Some providers return local timestamps without offset.
        }

        try {
            LocalDateTime arrival = LocalDateTime.parse(arrivalAt);
            LocalDateTime departure = LocalDateTime.parse(departureAt);
            Duration duration = Duration.between(arrival, departure);
            return duration.toString();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static FlightSearchResponseDTO.AirlineDTO resolveDuffelAirline(DuffelOfferListResponse.Slice slice) {
        if (slice == null || slice.segments() == null || slice.segments().isEmpty()) {
            return null;
        }

        DuffelOfferListResponse.Segment firstSegment = slice.segments().getFirst();
        DuffelOfferListResponse.Carrier carrier = firstSegment.operating_carrier() != null
                ? firstSegment.operating_carrier()
                : firstSegment.marketing_carrier();

        if (carrier == null) {
            return null;
        }

        String url = carrier.logo_symbol_url();
        if (url == null || url.isBlank()) {
            url = carrier.conditions_of_carriage_url();
        }

        return new FlightSearchResponseDTO.AirlineDTO(
                carrier.name(),
                carrier.iata_code(),
                url
        );
    }
}
