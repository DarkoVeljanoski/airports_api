package com.Airports.model.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {

    private Long id;

    private String codeOfStartingAirport;

    private String codeOfDestinationAirport;

    private int departureTimeInMinutes;

    private int flightDurationInMinutes;
}
