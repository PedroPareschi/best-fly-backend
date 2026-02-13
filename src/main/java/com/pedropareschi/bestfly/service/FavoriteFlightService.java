package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateFavoriteFlightRequest;
import com.pedropareschi.bestfly.dto.FavoriteFlightDTO;
import com.pedropareschi.bestfly.dto.UpdateFavoriteFlightRequest;
import com.pedropareschi.bestfly.entity.FavoriteFlight;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.FavoriteFlightRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FavoriteFlightService {

    private FavoriteFlightRepository favoriteFlightRepository;
    private UserRepository userRepository;

    public List<FavoriteFlightDTO> listFavoriteFlights(Long userId) {
        List<FavoriteFlight> favorites = userId == null
                ? favoriteFlightRepository.findAll()
                : favoriteFlightRepository.findByUserId(userId);
        return favorites.stream().map(FavoriteFlightService::toDTO).toList();
    }

    public Optional<FavoriteFlightDTO> getFavoriteFlight(Long id) {
        return favoriteFlightRepository.findById(id).map(FavoriteFlightService::toDTO);
    }

    public Optional<FavoriteFlightDTO> createFavoriteFlight(CreateFavoriteFlightRequest request) {
        if (request.userId() == null) {
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findById(request.userId());
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
        User user = favorite.getUser();
        if (request.userId() != null) {
            Optional<User> userOptional = userRepository.findById(request.userId());
            if (userOptional.isEmpty()) {
                return Optional.empty();
            }
            user = userOptional.get();
        }
        applyUpdateRequest(favorite, user, request);
        return Optional.of(toDTO(favoriteFlightRepository.save(favorite)));
    }

    public boolean deleteFavoriteFlight(Long id) {
        if (!favoriteFlightRepository.existsById(id)) {
            return false;
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

    private static void applyUpdateRequest(FavoriteFlight favorite, User user, UpdateFavoriteFlightRequest request) {
        favorite.setUser(user);
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
