package com.pedropareschi.bestfly.service;

import com.pedropareschi.bestfly.dto.PlaceDTO;
import com.pedropareschi.bestfly.dto.duffel.DuffelPlaceSuggestionsResponse;
import com.pedropareschi.bestfly.mapper.PlaceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {

    private DuffelService duffelService;

    public List<PlaceDTO> searchPlaces(String query) {
        DuffelPlaceSuggestionsResponse response = duffelService.listPlaceSuggestions(query);
        return PlaceMapper.mapDuffelPlaceSuggestions(response);
    }
}
