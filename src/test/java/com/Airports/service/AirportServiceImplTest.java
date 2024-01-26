package com.Airports.service;

import com.Airports.exception.AirportAlreadyExistException;
import com.Airports.exception.AirportDoesntExistException;
import com.Airports.exception.AirportInThatCountryDoesntExist;
import com.Airports.exception.AirportWithThatNameDoesntExistException;
import com.Airports.model.airport.Airport;
import com.Airports.model.airport.AirportRequest;
import com.Airports.model.airport.AirportResponse;
import com.Airports.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    @Spy
    private AirportServiceImpl airportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        airportService = new AirportServiceImpl(airportRepository, modelMapper);
    }

    private List<Airport> listOfAirports(){
        List<Airport> expectedAirports = Arrays.asList(
                new Airport(1L, "Airport1", "Country1", "AAA", 1000000.0),
                new Airport(2L, "Airport2", "Country2", "BBB", 2000000.0)
        );
        return expectedAirports;
    }

    private List<Airport> listOfAirportsWithSameCountry(){
        List<Airport> expectedAirports = Arrays.asList(
                new Airport(1L, "Airport1", "Country1", "AAA", 1000000.0),
                new Airport(2L, "Airport2", "Country1", "BBB", 2000000.0)
        );
        return expectedAirports;
    }

    private AirportRequest airportRequest(){
        AirportRequest airportRequest = new AirportRequest("Airport1", "Country1", "AAA", 1000000.0);
        return airportRequest;
    }

    @Test
    void saveCsvDataIntoAirport() {
    }

    @Test
    void testListAllAirports() {
        List<Airport> expectedAirports = listOfAirports();
        when(airportRepository.findAll()).thenReturn(expectedAirports);

        List<AirportResponse> actualAirportResponses = airportService.listAllAirports();

        assertEquals(expectedAirports.get(0).getAirportName(), actualAirportResponses.get(0).getAirportName());
        assertEquals(expectedAirports.get(0).getAirportCode(), actualAirportResponses.get(0).getAirportCode());
        assertEquals(expectedAirports.get(1).getAirportName(), actualAirportResponses.get(1).getAirportName());
        assertEquals(expectedAirports.get(1).getAirportCode(), actualAirportResponses.get(1).getAirportCode());
    }

    @Test
    void testGetAirportByName() {
        List<Airport> expectedAirports = listOfAirports();

        when(airportRepository.findByAirportName(anyString())).thenReturn(Optional.ofNullable(expectedAirports.get(0)));

        AirportResponse actualAirportResponse = airportService.getAirportByName("Airport1");

        assertEquals(expectedAirports.get(0).getAirportName(), actualAirportResponse.getAirportName());
    }

    @Test
    void testGetAirportByName_when_notFound(){
        when(airportRepository.findByAirportName("Airport1")).thenReturn(Optional.empty());

        assertThrows(AirportWithThatNameDoesntExistException.class, () -> airportService.getAirportByName("Airport1"));
    }


    @Test
    void testGetAirportsByCountry() {
        List<Airport> expectedAirports = listOfAirportsWithSameCountry();

        when(airportRepository.findByCountryName(anyString())).thenReturn(expectedAirports);

        List<AirportResponse> actualAirportResponses = airportService.getAirportsByCountry("Country1");

        assertEquals(expectedAirports.get(0).getAirportName(), actualAirportResponses.get(0).getAirportName());
        assertEquals(expectedAirports.get(0).getCountryName(), actualAirportResponses.get(0).getCountryName());
        assertEquals(expectedAirports.get(1).getCountryName(), actualAirportResponses.get(1).getCountryName());
    }

    @Test
    void testGetAirportsByCountry_when_AirportInThatCountryDoesNotExist(){
        when(airportRepository.findByCountryName("Country2")).thenReturn(Collections.emptyList());

        assertThrows(AirportInThatCountryDoesntExist.class, () -> airportService.getAirportsByCountry("Country2"));

    }

    @Test
    void testGetAirportWithMostPassenger() {
        List<Airport> expectedAirports = listOfAirportsWithSameCountry();

        when(airportRepository.findByCountryName("Country1")).thenReturn(expectedAirports);

        AirportResponse actualAirportResponse = airportService.getAirportWithMostPassenger("Country1");

        assertEquals(expectedAirports.get(1).getNumberOfPassengerAnnually(), actualAirportResponse.getNumberOfPassengerAnnually());
        assertEquals(expectedAirports.get(1).getAirportName(), actualAirportResponse.getAirportName());
    }

    @Test
    void testAddNewAirport() {
        List<Airport> expectedAirports = listOfAirports();

        when(airportRepository.save(any())).thenReturn(expectedAirports.get(0));
        when(airportRepository.findByAirportName(any())).thenReturn(Optional.empty());
        when(airportRepository.findByAirportCode(any())).thenReturn(Optional.empty());

        AirportResponse actualAirportResponse = airportService.addNewAirport(airportRequest());

        assertEquals(expectedAirports.get(0).getAirportName(), actualAirportResponse.getAirportName());
        assertEquals(expectedAirports.get(0).getAirportCode(), actualAirportResponse.getAirportCode());
    }

    @Test
    void testAddNewAirport_when_AirportAlreadyExistByName(){
        when(airportRepository.findByAirportName(anyString())).thenReturn(Optional.ofNullable(listOfAirports().get(0)));

        assertThrows(AirportAlreadyExistException.class, ()-> airportService.addNewAirport(airportRequest()));
    }


    @Test
    void testDeleteAirportById() {
        List<Airport> expectedAirports = listOfAirports();
        when(airportRepository.findById(expectedAirports.get(0).getId())).thenReturn(Optional.ofNullable(expectedAirports.get(0)));
        AirportResponse actualAirport = airportService.deleteAirportById(expectedAirports.get(0).getId());

        verify(airportRepository, times(1)).delete(expectedAirports.get(0));
        assertEquals(expectedAirports.get(0).getAirportName(), actualAirport.getAirportName());
        assertEquals(expectedAirports.get(0).getAirportCode(), actualAirport.getAirportCode());
    }

    @Test
    void testDeleteAirportBy_when_AirportNotFound(){
        when(airportRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(AirportDoesntExistException.class, () -> airportService.deleteAirportById(3L));
    }

    @Test
    void testGetAirportByCode() {
        List<Airport> expectedAirports = listOfAirports();

        when(airportRepository.findByAirportCode("AAA")).thenReturn(Optional.ofNullable(expectedAirports.get(0)));

        AirportResponse actualAirportResponse = airportService.getAirportByCode("AAA");

        assertEquals(expectedAirports.get(0).getAirportName(), actualAirportResponse.getAirportName());
        assertEquals(expectedAirports.get(0).getAirportCode(), actualAirportResponse.getAirportCode());
    }

    @Test
    void testGetAirportByCode_when_AirportDoesNotExist(){
        when(airportRepository.findByAirportCode("CCC")).thenReturn(Optional.empty());

        assertThrows(AirportDoesntExistException.class, () -> airportService.getAirportByCode("CCC"));
    }
}