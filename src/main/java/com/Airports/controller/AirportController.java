package com.Airports.controller;

import com.Airports.model.airport.AirportRequest;
import com.Airports.model.airport.AirportResponse;
import com.Airports.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping("/addDataFromCsv")
    public void addDataFromCsv(){
        airportService.saveCsvDataIntoAirport();
    }

    @PostMapping()
    public AirportResponse addNewAirport(@RequestBody AirportRequest airportRequest){
        AirportResponse airportResponse = airportService.addNewAirport(airportRequest);
        return airportResponse;
    }

    @GetMapping()
    public List<AirportResponse> listAllAirports(){
        List<AirportResponse> airportsResponse = airportService.listAllAirports();
        return airportsResponse;
    }

    @GetMapping("/byName/{airportName}")
    public AirportResponse getAirportByName(@PathVariable String airportName ){
        AirportResponse airportResponse = airportService.getAirportByName(airportName);
        return airportResponse;
    }

    @GetMapping("/byCountry/{country}")
    public List<AirportResponse> getAirportsByCountry(@PathVariable String country){
        List<AirportResponse> airportResponses = airportService.getAirportsByCountry(country);
        return airportResponses;
    }

    @GetMapping("/mostPassenger/{country}")
    public AirportResponse getAirportWithMostPassengers(@PathVariable String country){
        AirportResponse airportResponse = airportService.getAirportWithMostPassenger(country);
        return airportResponse;
    }

    @DeleteMapping("/{id}")
    public AirportResponse deleteAirportById(@PathVariable Long id){
        return airportService.deleteAirportById(id);
    }
}
