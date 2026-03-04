package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.response.PlaceResponse;
import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import com.pedropareschi.bestfly.config.CacheConfig;
import com.pedropareschi.bestfly.mapper.PlaceMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {

    private DuffelService duffelService;

    @Cacheable(
            cacheNames = CacheConfig.PLACE_SUGGESTIONS_CACHE,
            key = "#query",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<PlaceResponse> searchPlaces(String query) {
        DuffelPlaceSuggestionsResponse response = duffelService.listPlaceSuggestions(query);
        return PlaceMapper.mapDuffelPlaceSuggestions(response);
    }
}
