package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.LocationResponseDTO;
import com.pedropareschi.bestfly.dto.enums.LocationSubType;
import com.pedropareschi.bestfly.mapper.LocationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class LocationService {

    private AmadeusService amadeusService;

    public List<LocationResponseDTO> searchLocations(String keyword, LocationSubType subType) throws ResponseException {
        List<LocationResponseDTO> response = new ArrayList<>();
        response.addAll(LocationMapper.fromAmadeus(amadeusService.searchLocations(keyword, subType)));
        return response;
    }
}
