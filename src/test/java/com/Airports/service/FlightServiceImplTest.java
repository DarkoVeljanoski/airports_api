package com.Airports.service;

import com.Airports.model.airport.Airport;
import com.Airports.model.airport.AirportResponse;
import com.Airports.model.flight.AllFlightsFromAirportResponse;
import com.Airports.model.flight.Flight;
import com.Airports.model.flight.FlightRequest;
import com.Airports.model.flight.FlightResponse;
import com.Airports.repository.AirportRepository;
import com.Airports.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportService airportService;

    @Mock
    private AirportRepository airportRepository;

    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        flightService = new FlightServiceImpl(flightRepository,airportService, modelMapper);
    }

    private List<Flight> listOfFlights(){
        List<Flight> expectedFlights = Arrays.asList(
                new Flight(1L, "AAA", "BBB", 100, 200),
                new Flight(2L, "BBB", "AAA", 2000, 200)
        );
        return expectedFlights;
    }

    private List<FlightResponse> listOFFlightResponse(){
        List<FlightResponse> expectedFlights = Arrays.asList(
                new FlightResponse(1L, "AAA", "BBB", 100, 200),
                new FlightResponse(2L, "BBB", "AAA", 2000, 200)
        );
        return expectedFlights;
    }

    private List<Airport> listOfAirports(){
        List<Airport> expectedAirports = Arrays.asList(
                new Airport(1L, "Airport1", "Country1", "AAA", 1000000.0),
                new Airport(2L, "Airport2", "Country2", "BBB", 2000000.0)
        );
        return expectedAirports;
    }

    private List<AirportResponse> listOfAirportsResponse(){
        List<AirportResponse> expectedAirports = Arrays.asList(
                new AirportResponse(1L, "Airport1", "Country1", "AAA", 1000000.0),
                new AirportResponse(2L, "Airport2", "Country2", "BBB", 2000000.0)
        );
        return expectedAirports;
    }

    private FlightRequest flightRequest(){
        FlightRequest flightRequest = new FlightRequest("AAA", "BBB", 100, 200);
        return flightRequest;
    }

    private AllFlightsFromAirportResponse allFlightsFromAirportResponse(){
        AllFlightsFromAirportResponse allFlightsFromAirportResponse =
                new AllFlightsFromAirportResponse("Airport1", listOFFlightResponse(), listOFFlightResponse());
        return allFlightsFromAirportResponse;
    }

    @Test
    void testListAllFlights() {
        List<Flight> expectedFlights = listOfFlights();

        when(flightRepository.findAll()).thenReturn(expectedFlights);

        List<FlightResponse> flightResponses = flightService.listAllFlights();

        assertEquals(expectedFlights.get(0).getId(), flightResponses.get(0).getId());
        assertEquals(expectedFlights.get(0).getCodeOfStartingAirport(), flightResponses.get(0).getCodeOfStartingAirport());
    }

    @Test
    void testAddNewFlight() {
        List<Flight> expectedFlights = listOfFlights();
        when(flightRepository.save(any())).thenReturn(expectedFlights.get(0));

        FlightResponse actualFlight = flightService.addNewFlight(flightRequest());

        assertEquals(expectedFlights.get(0).getCodeOfStartingAirport(), actualFlight.getCodeOfStartingAirport());
        assertEquals(expectedFlights.get(0).getCodeOfDestinationAirport(), actualFlight.getCodeOfDestinationAirport());
    }

    @Test
    void testGetDirectFlights() {

        List<Flight> DirectFlights = listOfFlights();
        Flight expectedDirectFlight = DirectFlights.get(0);

        when(flightRepository.findAllByCodeOfStartingAirportAndCodeOfDestinationAirport("AAA","BBB"))
                .thenReturn(Collections.singletonList(expectedDirectFlight));

        List<FlightResponse> flightResponses = flightService.getDirectFlights("AAA","BBB");

        assertEquals(expectedDirectFlight.getCodeOfStartingAirport(), flightResponses.get(0).getCodeOfStartingAirport());
        assertEquals(expectedDirectFlight.getCodeOfDestinationAirport(), flightResponses.get(0).getCodeOfDestinationAirport());
        assertEquals(expectedDirectFlight.getDepartureTimeInMinutes(), flightResponses.get(0).getDepartureTimeInMinutes());
    }

    @Test
    void testGetAllFlightsFromAirportByCode() {
        AllFlightsFromAirportResponse flightsFromAirportResponse = allFlightsFromAirportResponse();
        List<Flight> expectedFlight = listOfFlights();
        when(flightRepository.findAllByCodeOfStartingAirportOrderByCodeOfDestinationAirportAsc(any())).thenReturn(expectedFlight);
        when(flightRepository.findAllByCodeOfStartingAirportOrderByDepartureTimeInMinutesAsc(any())).thenReturn(expectedFlight);
        when(airportRepository.findByAirportCode(any())).thenReturn(Optional.ofNullable(listOfAirports().get(0)));
        when(airportService.getAirportByCode(any())).thenReturn(listOfAirportsResponse().get(0));

        AllFlightsFromAirportResponse actual = flightService.getAllFlightsFromAirportByCode("AAA");
        actual.setAirportName(listOfAirportsResponse().get(0).getAirportName());

        assertEquals(flightsFromAirportResponse.getAirportName(), actual.getAirportName());
        assertEquals(flightsFromAirportResponse.getFlightsOrderedByDestinationCode().get(0).getCodeOfStartingAirport(),
                actual.getFlightsOrderedByDestinationCode().get(0).getCodeOfStartingAirport());

        assertEquals(flightsFromAirportResponse.getFlightOrderedByDeparture().get(0).getCodeOfStartingAirport(),
                actual.getFlightOrderedByDeparture().get(0).getCodeOfStartingAirport());
    }
}