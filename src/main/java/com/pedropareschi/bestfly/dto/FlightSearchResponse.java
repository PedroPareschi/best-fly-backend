package com.pedropareschi.bestfly.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightSearchResponse {
    private List<FlightInfo> departures;
    private List<FlightInfo> returns;
}
