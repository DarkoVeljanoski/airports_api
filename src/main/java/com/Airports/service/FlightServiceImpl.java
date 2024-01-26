package com.Airports.service;

import com.Airports.model.airport.AirportResponse;
import com.Airports.model.flight.AllFlightsFromAirportResponse;
import com.Airports.model.flight.Flight;
import com.Airports.model.flight.FlightRequest;
import com.Airports.model.flight.FlightResponse;
import com.Airports.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService{

    private final FlightRepository flightRepository;

    private final AirportService airportService;

    private final ModelMapper modelMapper;
    private final String flightsCsvFileName = "src/main/resources/flights.csv";


    @Override
    public void saveCsvDataIntoFlight() {
        String line="";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(flightsCsvFileName));
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                Flight flight = new Flight();
                flight.setCodeOfStartingAirport(data[0]);
                flight.setCodeOfDestinationAirport(data[1]);
                flight.setDepartureTimeInMinutes(Integer.parseInt(data[2]));
                flight.setFlightDurationInMinutes(Integer.parseInt(data[3]));
                flightRepository.save(flight);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FlightResponse> listAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return mapFlightListToFlightResponses(flights);
    }

    @Override
    public FlightResponse addNewFlight(FlightRequest flightRequest) {
        Flight flight = modelMapper.map(flightRequest, Flight.class);
        checkForExceptionOnAddNewFlight(flight);
        flightRepository.save(flight);
        return mapFlightToFlightResponse(flight);
    }
    @Override
    public List<FlightResponse> getDirectFlights(String codeStart, String codeDestination) {
        List<Flight> flights =
                flightRepository.findAllByCodeOfStartingAirportAndCodeOfDestinationAirport(codeStart,codeDestination);
        return mapFlightListToFlightResponses(flights);
    }

    @Override
    public AllFlightsFromAirportResponse getAllFlightsFromAirportByCode(String codeStart) {
        AllFlightsFromAirportResponse allFlightsFromAirportResponse = new AllFlightsFromAirportResponse();
        AirportResponse airportResponse = airportService.getAirportByCode(codeStart);
        allFlightsFromAirportResponse.setAirportName(airportResponse.getAirportName());

        List<FlightResponse> flightsByDestination = findAndReturnFlightByDestination(codeStart);

        List<FlightResponse> flightByDeparture = findAndReturnFlightByDeparture(codeStart);

        allFlightsFromAirportResponse.setFlightsOrderedByDestinationCode(flightsByDestination);
        allFlightsFromAirportResponse.setFlightOrderedByDeparture(flightByDeparture);
        return allFlightsFromAirportResponse;
    }

    private List<FlightResponse> findAndReturnFlightByDeparture(String codeStart) {
        List<Flight> flights = flightRepository.findAllByCodeOfStartingAirportOrderByDepartureTimeInMinutesAsc(codeStart);
        List<FlightResponse> flightResponsesDeparture = mapFlightListToFlightResponses(flights);
        return flightResponsesDeparture;
    }

    private List<FlightResponse> findAndReturnFlightByDestination(String codeStart) {
        List<Flight> flights = flightRepository.findAllByCodeOfStartingAirportOrderByCodeOfDestinationAirportAsc(codeStart);
        List<FlightResponse> flightResponsesDestination = mapFlightListToFlightResponses(flights);
        return flightResponsesDestination;
    }

    private List<FlightResponse> mapFlightListToFlightResponses(List<Flight> flights){
        return flights.stream().map(f -> modelMapper.map(f, FlightResponse.class)).collect(Collectors.toList());
    }

    private FlightResponse mapFlightToFlightResponse(Flight flight){
        return modelMapper.map(flight, FlightResponse.class);
    }

    private void checkForExceptionOnAddNewFlight(Flight flight) {

        airportService.getAirportByCode(flight.getCodeOfStartingAirport());
        airportService.getAirportByCode(flight.getCodeOfDestinationAirport());

    }

}
