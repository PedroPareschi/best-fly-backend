package com.pedropareschi.bestfly.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightSearchResponse {
    private List<FlightInfoResponse> departures;
    private List<FlightInfoResponse> returns;
}
