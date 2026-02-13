package com.pedropareschi.bestfly.service;

import com.amadeus.exceptions.ResponseException;
import com.pedropareschi.bestfly.dto.LocationDTO;
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

    public List<LocationDTO> searchLocations(String keyword, LocationSubType subType) throws ResponseException {
        return new ArrayList<>(LocationMapper.fromAmadeus(amadeusService.searchLocations(keyword, subType)));
    }
}
