package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.CreateSearchHistoryRequest;
import com.pedropareschi.bestfly.dto.SearchHistoryDTO;
import com.pedropareschi.bestfly.entity.SearchHistory;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.SearchHistoryRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SearchHistoryService {

    private SearchHistoryRepository searchHistoryRepository;
    private UserRepository userRepository;

    public List<SearchHistoryDTO> listSearchHistory() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<SearchHistory> history = searchHistoryRepository.findByUserId(currentUserId);
        return history.stream().map(SearchHistoryService::toDTO).toList();
    }

    public Optional<SearchHistoryDTO> getSearchHistory(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return searchHistoryRepository.findById(id)
                .filter(history -> history.getUser().getId().equals(currentUserId))
                .map(SearchHistoryService::toDTO);
    }

    public void createSearchHistory(CreateSearchHistoryRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Optional<User> userOptional = userRepository.findById(currentUserId);
        if (userOptional.isEmpty()) {
            return;
        }
        SearchHistory history = new SearchHistory();
        applyCreateRequest(history, userOptional.get(), request);
        toDTO(searchHistoryRepository.save(history));
    }

    public boolean deleteSearchHistory(Long id) {
        Optional<SearchHistory> historyOptional = searchHistoryRepository.findById(id);
        if (historyOptional.isEmpty()) {
            return false;
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!historyOptional.get().getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
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
