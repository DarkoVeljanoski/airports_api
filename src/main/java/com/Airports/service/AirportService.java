package com.Airports.service;

import com.Airports.model.airport.AirportRequest;
import com.Airports.model.airport.AirportResponse;

import java.util.List;

public interface AirportService {
    void saveCsvDataIntoAirport();

    List<AirportResponse> listAllAirports();

    AirportResponse getAirportByName(String airportName);

    List<AirportResponse> getAirportsByCountry(String country);
    AirportResponse getAirportWithMostPassenger(String country);

    AirportResponse addNewAirport(AirportRequest airportRequest);

    AirportResponse deleteAirportById(Long id);

    AirportResponse getAirportByCode(String code);
}
