package com.Airports.model.airport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportRequest {

    private String airportName;
    private String countryName;
    private String airportCode;
    private double numberOfPassengerAnnually;
}
