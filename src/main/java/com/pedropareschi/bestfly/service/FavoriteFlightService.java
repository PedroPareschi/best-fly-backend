package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.FavoriteFlightDTO;
import com.pedropareschi.bestfly.dto.FlightSearchResponse;
import com.pedropareschi.bestfly.dto.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.entity.FavoriteFlight;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.FavoriteFlightRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FavoriteFlightService {

    private FavoriteFlightRepository favoriteFlightRepository;
    private UserRepository userRepository;

    public List<FavoriteFlightDTO> listFavoriteFlights() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<FavoriteFlight> favorites = favoriteFlightRepository.findByUserId(currentUserId);
        return favorites.stream().map(FavoriteFlightService::toDTO).toList();
    }

    public Optional<FavoriteFlightDTO> getFavoriteFlight(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return favoriteFlightRepository.findById(id)
                .filter(favorite -> favorite.getUser().getId().equals(currentUserId))
                .map(FavoriteFlightService::toDTO);
    }

    public Optional<FavoriteFlightDTO> createFavoriteFlight(CreateFavoriteFlightRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Optional<User> userOptional = userRepository.findById(currentUserId);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        FavoriteFlight favorite = new FavoriteFlight();
        applyCreateRequest(favorite, userOptional.get(), request);
        return Optional.of(toDTO(favoriteFlightRepository.save(favorite)));
    }

    public Optional<FavoriteFlightDTO> updateFavoriteFlight(Long id, UpdateFavoriteFlightRequest request) {
        Optional<FavoriteFlight> favoriteOptional = favoriteFlightRepository.findById(id);
        if (favoriteOptional.isEmpty()) {
            return Optional.empty();
        }
        FavoriteFlight favorite = favoriteOptional.get();
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!favorite.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        applyUpdateRequest(favorite, request);
        return Optional.of(toDTO(favoriteFlightRepository.save(favorite)));
    }

    public boolean deleteFavoriteFlight(Long id) {
        Optional<FavoriteFlight> favoriteOptional = favoriteFlightRepository.findById(id);
        if (favoriteOptional.isEmpty()) {
            return false;
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!favoriteOptional.get().getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        favoriteFlightRepository.deleteById(id);
        return true;
    }

    private static void applyCreateRequest(FavoriteFlight favorite, User user, CreateFavoriteFlightRequest request) {
        favorite.setUser(user);
        applyOffer(favorite, request.offer());
        favorite.setCreatedAt(LocalDateTime.now());
    }

    private static void applyUpdateRequest(FavoriteFlight favorite, UpdateFavoriteFlightRequest request) {
        applyOffer(favorite, request.offer());
    }

    private static FavoriteFlightDTO toDTO(FavoriteFlight favorite) {
        return new FavoriteFlightDTO(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getCreatedAt(),
                favorite.getPriceAmount(),
                favorite.getPriceCurrency(),
                favorite.getAirlineName(),
                favorite.getAirlineIata(),
                favorite.getAirlineLogoUrl(),
                favorite.getOutboundOriginAirportCode(),
                favorite.getOutboundOriginCity(),
                favorite.getOutboundDestinationAirportCode(),
                favorite.getOutboundDestinationCity(),
                favorite.getOutboundDepartureAt(),
                favorite.getOutboundArrivalAt(),
                favorite.getOutboundDuration(),
                favorite.getOutboundStopsCount(),
                favorite.getInboundDepartureAt(),
                favorite.getInboundArrivalAt(),
                favorite.getInboundDuration(),
                favorite.getInboundStopsCount()
        );
    }

    private static void applyOffer(FavoriteFlight favorite, FlightSearchResponse.DuffelFlightOfferDTO offer) {
        if (offer == null) {
            return;
        }

        FlightSearchResponse.PriceDTO price = offer.price();
        if (price != null) {
            favorite.setPriceAmount(price.amount() != null ? new java.math.BigDecimal(price.amount()) : null);
            favorite.setPriceCurrency(price.currency());
        }

        FlightSearchResponse.AirlineDTO airline = offer.airline();
        if (airline != null) {
            favorite.setAirlineName(airline.name());
            favorite.setAirlineIata(airline.iata());
            favorite.setAirlineLogoUrl(airline.logo_url());
        }

        FlightSearchResponse.SliceDTO outbound = offer.outbound();
        if (outbound != null) {
            FlightSearchResponse.PlaceDTO origin = outbound.origin();
            FlightSearchResponse.PlaceDTO destination = outbound.destination();
            if (origin != null) {
                favorite.setOutboundOriginAirportCode(origin.airportCode());
                favorite.setOutboundOriginCity(origin.city());
            }
            if (destination != null) {
                favorite.setOutboundDestinationAirportCode(destination.airportCode());
                favorite.setOutboundDestinationCity(destination.city());
            }
            favorite.setOutboundDepartureAt(outbound.departureAt());
            favorite.setOutboundArrivalAt(outbound.arrivalAt());
            favorite.setOutboundDuration(outbound.duration());
            favorite.setOutboundStopsCount(outbound.stopsCount());
        }

        FlightSearchResponse.SliceDTO inbound = offer.inbound();
        if (inbound != null) {
            favorite.setInboundDepartureAt(inbound.departureAt());
            favorite.setInboundArrivalAt(inbound.arrivalAt());
            favorite.setInboundDuration(inbound.duration());
            favorite.setInboundStopsCount(inbound.stopsCount());
        } else {
            favorite.setInboundDepartureAt(null);
            favorite.setInboundArrivalAt(null);
            favorite.setInboundDuration(null);
            favorite.setInboundStopsCount(null);
        }
    }
}
