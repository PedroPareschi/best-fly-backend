package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.duffel.DuffelOfferListResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelOfferRequestResponse;
import com.pedropareschi.bestfly.entity.PriceAlertSubscription;
import com.pedropareschi.bestfly.repository.PriceAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceAlertProcessor {

    private final PriceAlertRepository priceAlertRepository;
    private final DuffelService duffelService;
    private final EmailService emailService;

    @Value("${price.alerts.sort:total_amount}")
    private String sort;

    @Scheduled(cron = "${price.alerts.cron:0 */30 * * * *}")
    @Transactional
    public void processAlerts() {
        List<PriceAlertSubscription> alerts = priceAlertRepository.findByActiveTrue();
        for (PriceAlertSubscription alert : alerts) {
            processAlert(alert);
        }
    }

    private void processAlert(PriceAlertSubscription alert) {
        String departureDate = alert.getDepartureDate() != null ? alert.getDepartureDate().toString() : null;
        String returnDate = alert.getReturnDate() != null ? alert.getReturnDate().toString() : null;

        DuffelOfferRequestResponse offerRequestResponse = duffelService.createOfferRequest(
                alert.getOriginLocation(),
                alert.getDestinationLocation(),
                departureDate,
                alert.getDepartureTime(),
                alert.getNumberOfAdults(),
                alert.getNumberOfChildren(),
                returnDate,
                alert.getReturnTime()
        );

        String offerRequestId = offerRequestResponse != null && offerRequestResponse.data() != null
                ? offerRequestResponse.data().id()
                : null;

        if (offerRequestId == null) {
            alert.setLastCheckedAt(LocalDateTime.now());
            priceAlertRepository.save(alert);
            return;
        }

        DuffelOfferListResponse offerListResponse = duffelService.listOffers(offerRequestId, 1, null, sort);
        if (offerListResponse == null || offerListResponse.data() == null || offerListResponse.data().isEmpty()) {
            alert.setLastCheckedAt(LocalDateTime.now());
            priceAlertRepository.save(alert);
            return;
        }

        DuffelOfferListResponse.Offer lowestOffer = offerListResponse.data().get(0);
        BigDecimal lowestAmount = parseAmount(lowestOffer.total_amount());
        String lowestCurrency = lowestOffer.total_currency();

        alert.setLastLowestPriceAmount(lowestAmount);
        alert.setLastLowestPriceCurrency(lowestCurrency);
        alert.setLastCheckedAt(LocalDateTime.now());

        if (lowestAmount == null || lowestCurrency == null || lowestCurrency.isBlank()) {
            priceAlertRepository.save(alert);
            return;
        }

        boolean currencyMatches = alert.getTargetPriceCurrency() == null
                || alert.getTargetPriceCurrency().isBlank()
                || alert.getTargetPriceCurrency().equalsIgnoreCase(lowestCurrency);

        if (!currencyMatches) {
            priceAlertRepository.save(alert);
            return;
        }

        boolean shouldNotify;
        if (alert.getTargetPriceAmount() != null) {
            shouldNotify = lowestAmount.compareTo(alert.getTargetPriceAmount()) <= 0
                    && (alert.getLastNotifiedPriceAmount() == null
                    || lowestAmount.compareTo(alert.getLastNotifiedPriceAmount()) < 0);
        } else {
            shouldNotify = alert.getLastNotifiedPriceAmount() == null
                    || lowestAmount.compareTo(alert.getLastNotifiedPriceAmount()) < 0;
        }

        if (shouldNotify) {
            String carrierName = resolveCarrierName(lowestOffer);
            emailService.sendPriceAlert(alert.getUser(), alert, lowestAmount, lowestCurrency, carrierName);
            alert.setLastNotifiedPriceAmount(lowestAmount);
            alert.setLastNotifiedPriceCurrency(lowestCurrency);
            alert.setLastNotifiedAt(LocalDateTime.now());
        }

        priceAlertRepository.save(alert);
    }

    private static BigDecimal parseAmount(String amount) {
        if (amount == null || amount.isBlank()) {
            return null;
        }
        return new BigDecimal(amount);
    }

    private static String resolveCarrierName(DuffelOfferListResponse.Offer offer) {
        if (offer.slices() == null || offer.slices().isEmpty()) {
            return null;
        }
        DuffelOfferListResponse.Slice firstSlice = offer.slices().get(0);
        if (firstSlice.segments() == null || firstSlice.segments().isEmpty()) {
            return null;
        }
        DuffelOfferListResponse.Segment firstSegment = firstSlice.segments().get(0);
        DuffelOfferListResponse.Carrier carrier = firstSegment.operating_carrier() != null
                ? firstSegment.operating_carrier()
                : firstSegment.marketing_carrier();
        return carrier != null ? carrier.name() : null;
    }
}
