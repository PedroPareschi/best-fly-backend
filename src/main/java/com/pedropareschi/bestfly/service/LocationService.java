package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.LocationResponse;
import com.pedropareschi.bestfly.mapper.LocationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class LocationService {

    private AmadeusService amadeusService;

    public List<LocationResponse> searchLocations(String keyword) throws ResponseException {
        List<LocationResponse> response = new ArrayList<>();
        response.addAll(LocationMapper.fromAmadeus(amadeusService.searchLocations(keyword)));
        return response;
    }
}
