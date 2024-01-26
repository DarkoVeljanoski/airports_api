package com.Airports.model.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllFlightsFromAirportResponse {

    private String airportName;

    private List<FlightResponse> flightsOrderedByDestinationCode;

    private List<FlightResponse> flightOrderedByDeparture;
}
