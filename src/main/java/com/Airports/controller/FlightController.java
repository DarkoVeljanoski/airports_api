package com.Airports.controller;

import com.Airports.model.flight.AllFlightsFromAirportResponse;
import com.Airports.model.flight.FlightRequest;
import com.Airports.model.flight.FlightResponse;
import com.Airports.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/addDataFromCsv")
    public void addDataFromCsv(){
        flightService.saveCsvDataIntoFlight();
    }

    @GetMapping()
    public List<FlightResponse> listAllFlights(){
        List<FlightResponse> flightResponses = flightService.listAllFlights();
        return flightResponses;
    }

    @PostMapping()
    public FlightResponse addNewFlight(@RequestBody FlightRequest flightRequest){
        FlightResponse flightResponse = flightService.addNewFlight(flightRequest);
        return flightResponse;
    }

    @GetMapping("from/{codeStart}")
    public AllFlightsFromAirportResponse getAllFlightsFromSpecificAirportByCode(@PathVariable String codeStart){
        AllFlightsFromAirportResponse flights = flightService.getAllFlightsFromAirportByCode(codeStart);
        return flights;

    }

    @GetMapping("/from/{codeStart}/to/{codeDestination}")
    public List<FlightResponse> getDirectFlights(@PathVariable String codeStart, @PathVariable String codeDestination){
        List<FlightResponse> flightResponses = flightService.getDirectFlights(codeStart,codeDestination);
        return flightResponses;
    }


}
