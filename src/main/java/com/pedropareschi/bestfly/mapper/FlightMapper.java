package com.pedropareschi.bestfly.mapper;

import com.pedropareschi.bestfly.dto.AirlineDTO;
import com.pedropareschi.bestfly.dto.DuffelFlightSearchResponseDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlightMapper {

    public static List<DuffelFlightSearchResponseDTO.DuffelFlightOfferDTO> mapDuffelOffers(DuffelOfferListResponse offerListResponse) {
        if (offerListResponse == null || offerListResponse.data() == null) {
            return Collections.emptyList();
        }

        List<DuffelFlightSearchResponseDTO.DuffelFlightOfferDTO> offers = new ArrayList<>();
        for (DuffelOfferListResponse.Offer offer : offerListResponse.data()) {
            List<DuffelOfferListResponse.Slice> slices = offer.slices();
            if (slices == null || slices.isEmpty()) {
                continue;
            }

            DuffelOfferListResponse.Slice outboundSlice = slices.getFirst();
            DuffelOfferListResponse.Slice inboundSlice = slices.size() > 1 ? slices.get(1) : null;

            DuffelFlightSearchResponseDTO.SliceDTO outbound = mapDuffelSlice(outboundSlice);
            DuffelFlightSearchResponseDTO.SliceDTO inbound = inboundSlice != null ? mapDuffelSlice(inboundSlice) : null;

            AirlineDTO airline = resolveDuffelAirline(outboundSlice);

            DuffelFlightSearchResponseDTO.PriceDTO price = new DuffelFlightSearchResponseDTO.PriceDTO(
                    offer.total_amount(),
                    offer.total_currency()
            );

            offers.add(new DuffelFlightSearchResponseDTO.DuffelFlightOfferDTO(
                    price,
                    outbound,
                    inbound,
                    airline
            ));
        }

        return offers;
    }

    public static DuffelFlightSearchResponseDTO.PaginationDTO mapDuffelPagination(DuffelOfferListResponse offerListResponse, int limit) {
        return new DuffelFlightSearchResponseDTO.PaginationDTO(
                offerListResponse != null && offerListResponse.meta() != null ? offerListResponse.meta().after() : null,
                offerListResponse != null && offerListResponse.meta() != null ? offerListResponse.meta().before() : null,
                limit
        );
    }

    private static DuffelFlightSearchResponseDTO.SliceDTO mapDuffelSlice(DuffelOfferListResponse.Slice slice) {
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

        List<DuffelFlightSearchResponseDTO.StopDTO> stops = mapDuffelStops(segments);

        return new DuffelFlightSearchResponseDTO.SliceDTO(
                slice.origin() != null ? slice.origin().iata_code() : null,
                slice.destination() != null ? slice.destination().iata_code() : null,
                departureAt,
                arrivalAt,
                slice.duration(),
                stops.size(),
                stops
        );
    }

    private static List<DuffelFlightSearchResponseDTO.StopDTO> mapDuffelStops(List<DuffelOfferListResponse.Segment> segments) {
        if (segments == null || segments.size() <= 1) {
            return Collections.emptyList();
        }

        Map<String, DuffelFlightSearchResponseDTO.StopDTO> uniqueStops = new LinkedHashMap<>();

        for (int i = 1; i < segments.size(); i++) {
            DuffelOfferListResponse.Place place = segments.get(i).origin();
            addDuffelStop(uniqueStops, place);
        }

        for (DuffelOfferListResponse.Segment segment : segments) {
            if (segment.stops() == null) {
                continue;
            }
            for (DuffelOfferListResponse.Stop stop : segment.stops()) {
                addDuffelStop(uniqueStops, stop.airport());
            }
        }

        return new ArrayList<>(uniqueStops.values());
    }

    private static void addDuffelStop(Map<String, DuffelFlightSearchResponseDTO.StopDTO> uniqueStops, DuffelOfferListResponse.Place place) {
        if (place == null || place.iata_code() == null) {
            return;
        }
        uniqueStops.putIfAbsent(place.iata_code(), new DuffelFlightSearchResponseDTO.StopDTO(
                place.iata_code(),
                place.name(),
                place.city_name()
        ));
    }

    private static AirlineDTO resolveDuffelAirline(DuffelOfferListResponse.Slice slice) {
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

        return new AirlineDTO(
                carrier.name(),
                carrier.iata_code(),
                url
        );
    }
}
