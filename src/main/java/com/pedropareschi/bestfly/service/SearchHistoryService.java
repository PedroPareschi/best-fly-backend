package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.SearchHistoryDTO;
import com.pedropareschi.bestfly.dto.UpdateSearchHistoryRequest;
import com.pedropareschi.bestfly.entity.SearchHistory;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.SearchHistoryRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SearchHistoryService {

    private SearchHistoryRepository searchHistoryRepository;
    private UserRepository userRepository;

    public List<SearchHistoryDTO> listSearchHistory(Long userId) {
        List<SearchHistory> history = userId == null
                ? searchHistoryRepository.findAll()
                : searchHistoryRepository.findByUserId(userId);
        return history.stream().map(SearchHistoryService::toDTO).toList();
    }

    public Optional<SearchHistoryDTO> getSearchHistory(Long id) {
        return searchHistoryRepository.findById(id).map(SearchHistoryService::toDTO);
    }

    public Optional<SearchHistoryDTO> createSearchHistory(CreateSearchHistoryRequest request) {
        if (request.userId() == null) {
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findById(request.userId());
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        SearchHistory history = new SearchHistory();
        applyCreateRequest(history, userOptional.get(), request);
        return Optional.of(toDTO(searchHistoryRepository.save(history)));
    }

    public Optional<SearchHistoryDTO> updateSearchHistory(Long id, UpdateSearchHistoryRequest request) {
        Optional<SearchHistory> historyOptional = searchHistoryRepository.findById(id);
        if (historyOptional.isEmpty()) {
            return Optional.empty();
        }
        SearchHistory history = historyOptional.get();
        User user = history.getUser();
        if (request.userId() != null) {
            Optional<User> userOptional = userRepository.findById(request.userId());
            if (userOptional.isEmpty()) {
                return Optional.empty();
            }
            user = userOptional.get();
        }
        applyUpdateRequest(history, user, request);
        return Optional.of(toDTO(searchHistoryRepository.save(history)));
    }

    public boolean deleteSearchHistory(Long id) {
        if (!searchHistoryRepository.existsById(id)) {
            return false;
        }
        searchHistoryRepository.deleteById(id);
        return true;
    }

    private static void applyCreateRequest(SearchHistory history, User user, CreateSearchHistoryRequest request) {
        history.setUser(user);
        history.setOriginLocation(request.originLocation());
        history.setDestinationLocation(request.destinationLocation());
        history.setDepartureDate(request.departureDate());
        history.setNumberOfAdults(request.numberOfAdults());
        history.setReturnDate(request.returnDate());
        history.setCreatedAt(LocalDateTime.now());
    }

    private static void applyUpdateRequest(SearchHistory history, User user, UpdateSearchHistoryRequest request) {
        history.setUser(user);
        history.setOriginLocation(request.originLocation());
        history.setDestinationLocation(request.destinationLocation());
        history.setDepartureDate(request.departureDate());
        history.setNumberOfAdults(request.numberOfAdults());
        history.setReturnDate(request.returnDate());
    }

    private static SearchHistoryDTO toDTO(SearchHistory history) {
        return new SearchHistoryDTO(
                history.getId(),
                history.getUser().getId(),
                history.getOriginLocation(),
                history.getDestinationLocation(),
                history.getDepartureDate(),
                history.getNumberOfAdults(),
                history.getReturnDate(),
                history.getCreatedAt()
        );
    }
}
