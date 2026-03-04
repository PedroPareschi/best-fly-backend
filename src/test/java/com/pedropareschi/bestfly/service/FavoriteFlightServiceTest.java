package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.request.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.response.FavoriteFlightResponse;
import com.pedropareschi.bestfly.dto.response.FlightSearchResponse;
import com.pedropareschi.bestfly.entity.FavoriteFlight;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.FavoriteFlightRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteFlightServiceTest {

    @Mock
    private FavoriteFlightRepository favoriteFlightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoriteFlightService favoriteFlightService;

    @BeforeEach
    void setUpSecurity() {
        UserPrincipal principal = new UserPrincipal(1L, "user@email.com", "encoded");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
        );
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listFavoriteFlightsShouldReturnMappedFavorites() {
        FavoriteFlight favorite = favoriteEntity(1L, 1L);
        when(favoriteFlightRepository.findByUserId(1L)).thenReturn(List.of(favorite));

        List<FavoriteFlightResponse> responses = favoriteFlightService.listFavoriteFlights();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.getFirst().id());
        assertEquals("USD", responses.getFirst().priceCurrency());
        verify(favoriteFlightRepository).findByUserId(1L);
    }

    @Test
    void getFavoriteFlightShouldReturnEmptyWhenFavoriteBelongsToAnotherUser() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity(1L, 99L)));

        Optional<FavoriteFlightResponse> response = favoriteFlightService.getFavoriteFlight(1L);

        assertTrue(response.isEmpty());
    }

    @Test
    void createFavoriteFlightShouldReturnEmptyWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<FavoriteFlightResponse> response = favoriteFlightService.createFavoriteFlight(new CreateFavoriteFlightRequest(sampleOffer()));

        assertTrue(response.isEmpty());
        verify(favoriteFlightRepository, never()).save(any(FavoriteFlight.class));
    }

    @Test
    void createFavoriteFlightShouldSaveAndMapOffer() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(favoriteFlightRepository.save(any(FavoriteFlight.class))).thenAnswer(invocation -> {
            FavoriteFlight saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Optional<FavoriteFlightResponse> response = favoriteFlightService.createFavoriteFlight(new CreateFavoriteFlightRequest(sampleOffer()));

        assertTrue(response.isPresent());
        FavoriteFlightResponse dto = response.get();
        assertEquals(10L, dto.id());
        assertEquals(new BigDecimal("123.45"), dto.priceAmount());
        assertEquals("AA", dto.airlineIata());
        assertEquals("GRU", dto.outboundOriginAirportCode());
        assertEquals("PT2H", dto.outboundDuration());
        assertEquals("PT9H", dto.inboundDuration());
        assertNotNull(dto.createdAt());
    }

    @Test
    void updateFavoriteFlightShouldReturnEmptyWhenFavoriteMissing() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<FavoriteFlightResponse> response = favoriteFlightService.updateFavoriteFlight(1L, new UpdateFavoriteFlightRequest(sampleOffer()));

        assertTrue(response.isEmpty());
    }

    @Test
    void updateFavoriteFlightShouldThrowForbiddenWhenFavoriteBelongsToAnotherUser() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity(1L, 77L)));

        assertThrows(ResponseStatusException.class,
                () -> favoriteFlightService.updateFavoriteFlight(1L, new UpdateFavoriteFlightRequest(sampleOffer())));
    }

    @Test
    void deleteFavoriteFlightShouldReturnFalseWhenFavoriteMissing() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = favoriteFlightService.deleteFavoriteFlight(1L);

        assertFalse(deleted);
        verify(favoriteFlightRepository, never()).deleteById(1L);
    }

    @Test
    void deleteFavoriteFlightShouldThrowForbiddenWhenFavoriteBelongsToAnotherUser() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity(1L, 77L)));

        assertThrows(ResponseStatusException.class, () -> favoriteFlightService.deleteFavoriteFlight(1L));
    }

    @Test
    void deleteFavoriteFlightShouldDeleteWhenFavoriteBelongsToCurrentUser() {
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.of(favoriteEntity(1L, 1L)));

        boolean deleted = favoriteFlightService.deleteFavoriteFlight(1L);

        assertTrue(deleted);
        verify(favoriteFlightRepository).deleteById(1L);
    }

    @Test
    void updateFavoriteFlightShouldClearInboundWhenOfferHasNoInboundSlice() {
        FavoriteFlight existing = favoriteEntity(1L, 1L);
        when(favoriteFlightRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(favoriteFlightRepository.save(any(FavoriteFlight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FlightSearchResponse.DuffelFlightOfferDTO offerWithoutInbound = new FlightSearchResponse.DuffelFlightOfferDTO(
                sampleOffer().price(),
                sampleOffer().outbound(),
                null,
                sampleOffer().airline()
        );

        favoriteFlightService.updateFavoriteFlight(1L, new UpdateFavoriteFlightRequest(offerWithoutInbound));

        ArgumentCaptor<FavoriteFlight> captor = ArgumentCaptor.forClass(FavoriteFlight.class);
        verify(favoriteFlightRepository).save(captor.capture());
        FavoriteFlight saved = captor.getValue();
        assertNull(saved.getInboundDepartureAt());
        assertNull(saved.getInboundArrivalAt());
        assertNull(saved.getInboundDuration());
        assertNull(saved.getInboundStopsCount());
    }

    private FavoriteFlight favoriteEntity(Long id, Long userId) {
        User user = new User();
        user.setId(userId);

        FavoriteFlight favorite = new FavoriteFlight();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setPriceAmount(new BigDecimal("99.99"));
        favorite.setPriceCurrency("USD");
        favorite.setAirlineName("Airline");
        favorite.setAirlineIata("AA");
        favorite.setOutboundOriginAirportCode("GRU");
        favorite.setOutboundOriginCity("Sao Paulo");
        favorite.setOutboundDestinationAirportCode("JFK");
        favorite.setOutboundDestinationCity("New York");
        favorite.setOutboundDepartureAt("2026-01-01T10:00:00");
        favorite.setOutboundArrivalAt("2026-01-01T18:00:00");
        favorite.setOutboundDuration("PT8H");
        favorite.setOutboundStopsCount(0);
        favorite.setInboundDepartureAt("2026-01-10T10:00:00");
        favorite.setInboundArrivalAt("2026-01-10T18:00:00");
        favorite.setInboundDuration("PT8H");
        favorite.setInboundStopsCount(0);
        return favorite;
    }

    private FlightSearchResponse.DuffelFlightOfferDTO sampleOffer() {
        FlightSearchResponse.PriceDTO price = new FlightSearchResponse.PriceDTO("123.45", "USD");
        FlightSearchResponse.PlaceDTO origin = new FlightSearchResponse.PlaceDTO("GRU", "Guarulhos", "Sao Paulo");
        FlightSearchResponse.PlaceDTO destination = new FlightSearchResponse.PlaceDTO("JFK", "John F Kennedy", "New York");
        FlightSearchResponse.SliceDTO outbound = new FlightSearchResponse.SliceDTO(
                origin,
                destination,
                "2026-01-01T10:00:00",
                "2026-01-01T12:00:00",
                "PT2H",
                0,
                List.of()
        );
        FlightSearchResponse.SliceDTO inbound = new FlightSearchResponse.SliceDTO(
                destination,
                origin,
                "2026-01-10T10:00:00",
                "2026-01-10T19:00:00",
                "PT9H",
                1,
                List.of()
        );
        FlightSearchResponse.AirlineDTO airline = new FlightSearchResponse.AirlineDTO("Airline", "AA", "logo", "terms");
        return new FlightSearchResponse.DuffelFlightOfferDTO(price, outbound, inbound, airline);
    }
}
