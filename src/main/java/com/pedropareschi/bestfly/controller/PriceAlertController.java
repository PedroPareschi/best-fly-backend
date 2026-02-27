package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.CreatePriceAlertRequest;
import com.pedropareschi.bestfly.dto.request.UpdatePriceAlertRequest;
import com.pedropareschi.bestfly.dto.response.PriceAlertResponse;
import com.pedropareschi.bestfly.service.PriceAlertService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price-alerts")
@AllArgsConstructor
public class PriceAlertController {

    private PriceAlertService priceAlertService;

    @GetMapping
    public ResponseEntity<List<PriceAlertResponse>> listPriceAlerts() {
        return ResponseEntity.ok(priceAlertService.listPriceAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceAlertResponse> getPriceAlert(@PathVariable Long id) {
        return priceAlertService.getPriceAlert(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PriceAlertResponse> createPriceAlert(@RequestBody CreatePriceAlertRequest request) {
        return priceAlertService.createPriceAlert(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PriceAlertResponse> updatePriceAlert(
            @PathVariable Long id,
            @RequestBody UpdatePriceAlertRequest request
    ) {
        return priceAlertService.updatePriceAlert(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceAlert(@PathVariable Long id) {
        if (priceAlertService.deletePriceAlert(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
