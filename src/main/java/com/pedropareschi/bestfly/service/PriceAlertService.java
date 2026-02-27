package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.request.CreatePriceAlertRequest;
import com.pedropareschi.bestfly.dto.request.UpdatePriceAlertRequest;
import com.pedropareschi.bestfly.dto.response.PriceAlertResponse;
import com.pedropareschi.bestfly.entity.PriceAlertSubscription;
import com.pedropareschi.bestfly.entity.User;
import com.pedropareschi.bestfly.repository.PriceAlertRepository;
import com.pedropareschi.bestfly.repository.UserRepository;
import com.pedropareschi.bestfly.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceAlertService {

    private PriceAlertRepository priceAlertRepository;
    private UserRepository userRepository;

    public List<PriceAlertResponse> listPriceAlerts() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return priceAlertRepository.findByUserId(currentUserId).stream().map(PriceAlertService::toDTO).toList();
    }

    public Optional<PriceAlertResponse> getPriceAlert(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return priceAlertRepository.findById(id)
                .filter(alert -> alert.getUser().getId().equals(currentUserId))
                .map(PriceAlertService::toDTO);
    }

    public Optional<PriceAlertResponse> createPriceAlert(CreatePriceAlertRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Optional<User> userOptional = userRepository.findById(currentUserId);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        PriceAlertSubscription alert = new PriceAlertSubscription();
        applyCreateRequest(alert, userOptional.get(), request);
        return Optional.of(toDTO(priceAlertRepository.save(alert)));
    }

    public Optional<PriceAlertResponse> updatePriceAlert(Long id, UpdatePriceAlertRequest request) {
        Optional<PriceAlertSubscription> alertOptional = priceAlertRepository.findById(id);
        if (alertOptional.isEmpty()) {
            return Optional.empty();
        }
        PriceAlertSubscription alert = alertOptional.get();
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!alert.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        applyUpdateRequest(alert, request);
        return Optional.of(toDTO(priceAlertRepository.save(alert)));
    }

    public boolean deletePriceAlert(Long id) {
        Optional<PriceAlertSubscription> alertOptional = priceAlertRepository.findById(id);
        if (alertOptional.isEmpty()) {
            return false;
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!alertOptional.get().getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        priceAlertRepository.deleteById(id);
        return true;
    }

    private static void applyCreateRequest(PriceAlertSubscription alert, User user, CreatePriceAlertRequest request) {
        alert.setUser(user);
        alert.setActive(true);
        alert.setOriginLocation(request.originLocation());
        alert.setDestinationLocation(request.destinationLocation());
        alert.setDepartureDate(parseDate(request.departureDate()));
        alert.setDepartureTime(normalizeTime(request.departureTime()));
        alert.setReturnDate(parseDate(request.returnDate()));
        alert.setReturnTime(normalizeTime(request.returnTime()));
        alert.setNumberOfAdults(request.numberOfAdults() != null ? request.numberOfAdults() : 1);
        alert.setNumberOfChildren(request.numberOfChildren() != null ? request.numberOfChildren() : 0);
        alert.setTargetPriceAmount(request.targetPriceAmount());
        alert.setTargetPriceCurrency(normalizeCurrency(request.targetPriceCurrency()));
        alert.setCreatedAt(LocalDateTime.now());
    }

    private static void applyUpdateRequest(PriceAlertSubscription alert, UpdatePriceAlertRequest request) {
        if (request.originLocation() != null) {
            alert.setOriginLocation(request.originLocation());
        }
        if (request.destinationLocation() != null) {
            alert.setDestinationLocation(request.destinationLocation());
        }
        if (request.departureDate() != null) {
            alert.setDepartureDate(parseDate(request.departureDate()));
        }
        if (request.departureTime() != null) {
            alert.setDepartureTime(normalizeTime(request.departureTime()));
        }
        if (request.returnDate() != null) {
            alert.setReturnDate(parseDate(request.returnDate()));
        }
        if (request.returnTime() != null) {
            alert.setReturnTime(normalizeTime(request.returnTime()));
        }
        if (request.numberOfAdults() != null) {
            alert.setNumberOfAdults(request.numberOfAdults());
        }
        if (request.numberOfChildren() != null) {
            alert.setNumberOfChildren(request.numberOfChildren());
        }
        if (request.targetPriceAmount() != null) {
            alert.setTargetPriceAmount(request.targetPriceAmount());
        }
        if (request.targetPriceCurrency() != null) {
            alert.setTargetPriceCurrency(normalizeCurrency(request.targetPriceCurrency()));
        }
        if (request.active() != null) {
            alert.setActive(request.active());
        }
    }

    private static LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return LocalDate.parse(date);
    }

    private static String normalizeTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        return time;
    }

    private static String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return null;
        }
        return currency.toUpperCase();
    }

    private static PriceAlertResponse toDTO(PriceAlertSubscription alert) {
        return new PriceAlertResponse(
                alert.getId(),
                alert.getUser().getId(),
                alert.isActive(),
                alert.getOriginLocation(),
                alert.getDestinationLocation(),
                alert.getDepartureDate(),
                alert.getDepartureTime(),
                alert.getReturnDate(),
                alert.getReturnTime(),
                alert.getNumberOfAdults(),
                alert.getNumberOfChildren(),
                alert.getTargetPriceAmount(),
                alert.getTargetPriceCurrency(),
                alert.getLastLowestPriceAmount(),
                alert.getLastLowestPriceCurrency(),
                alert.getLastNotifiedPriceAmount(),
                alert.getLastNotifiedPriceCurrency(),
                alert.getLastCheckedAt(),
                alert.getLastNotifiedAt(),
                alert.getCreatedAt()
        );
    }
}
