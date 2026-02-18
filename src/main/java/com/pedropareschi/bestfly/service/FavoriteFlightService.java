package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.FavoriteFlightDTO;
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
        favorite.setPrice(request.price());
        favorite.setDetails(request.details());
        favorite.setCreatedAt(LocalDateTime.now());
    }

    private static void applyUpdateRequest(FavoriteFlight favorite, UpdateFavoriteFlightRequest request) {
        favorite.setPrice(request.price());
        favorite.setDetails(request.details());
    }

    private static FavoriteFlightDTO toDTO(FavoriteFlight favorite) {
        return new FavoriteFlightDTO(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getCreatedAt(),
                favorite.getPrice(),
                favorite.getDetails()
        );
    }
}
