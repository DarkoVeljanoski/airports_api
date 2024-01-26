package com.Airports.service;

import com.Airports.model.flight.AllFlightsFromAirportResponse;
import com.Airports.model.flight.FlightRequest;
import com.Airports.model.flight.FlightResponse;

import java.util.List;

public interface FlightService {

    void saveCsvDataIntoFlight();

    List<FlightResponse> listAllFlights();

    FlightResponse addNewFlight(FlightRequest flightRequest);

    List<FlightResponse> getDirectFlights(String codeStart, String codeDestination);

    AllFlightsFromAirportResponse getAllFlightsFromAirportByCode(String codeStart);
}
