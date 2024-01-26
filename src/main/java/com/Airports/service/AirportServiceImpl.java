package com.Airports.service;

import com.Airports.exception.AirportAlreadyExistException;
import com.Airports.exception.AirportDoesntExistException;
import com.Airports.exception.AirportInThatCountryDoesntExist;
import com.Airports.exception.AirportWithThatNameDoesntExistException;
import com.Airports.model.airport.Airport;
import com.Airports.model.airport.AirportRequest;
import com.Airports.model.airport.AirportResponse;
import com.Airports.repository.AirportRepository;
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
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    private final ModelMapper modelMapper;
    private final String airportsCsvFileName = "src/main/resources/airports.csv";

    public void saveCsvDataIntoAirport(){
        String line="";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(airportsCsvFileName));
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(";");
                Airport airport = new Airport();
                airport.setAirportName(data[0]);
                airport.setCountryName(data[1]);
                airport.setAirportCode(data[2]);
                airport.setNumberOfPassengerAnnually(Double.parseDouble(data[3]));
                airportRepository.save(airport);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AirportResponse> listAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        return mapAirportListToAirportResponse(airports);
    }

    @Override
    public AirportResponse getAirportByName(String airportName) {
        Airport airport = airportRepository.findByAirportName(airportName).orElseThrow(AirportWithThatNameDoesntExistException::new);
        return mapAirportToAirportResponse(airport);
    }

    private AirportResponse mapAirportToAirportResponse(Airport airport){
        return modelMapper.map(airport, AirportResponse.class);
    }

    @Override
    public List<AirportResponse> getAirportsByCountry(String country){
        List<Airport> airports = airportRepository.findByCountryName(country);
        checkIfAirportInThatCountryExistAndThrowException(airports);
        return mapAirportListToAirportResponse(airports);
    }

    private void checkIfAirportInThatCountryExistAndThrowException(List<Airport> airports){
        if (airports.size()==0){
            throw new AirportInThatCountryDoesntExist();
        }
    }

    private List<AirportResponse> mapAirportListToAirportResponse(List<Airport> airports){
        return airports.stream().map(a -> modelMapper.map(a, AirportResponse.class)).collect(Collectors.toList());
    }

    @Override
    public AirportResponse getAirportWithMostPassenger(String country) {
        List<AirportResponse> airportResponses = getAirportsByCountry(country);
        AirportResponse airportResponse = calculateMostPassengerAndReturnAirport(airportResponses);
        return airportResponse;
    }

    @Override
    public AirportResponse addNewAirport(AirportRequest airportRequest) {
        Airport airport = modelMapper.map(airportRequest, Airport.class);
        checkIfAirportAlreadyExistByNameOrCodeAndThrowException(airport);
        airportRepository.save(airport);
        return mapAirportToAirportResponse(airport);
    }

    @Override
    public AirportResponse deleteAirportById(Long id) {
        Airport airport = airportRepository.findById(id).orElseThrow(AirportDoesntExistException::new);
        airportRepository.delete(airport);
        return mapAirportToAirportResponse(airport);
    }

    @Override
    public AirportResponse getAirportByCode(String code) {
        Airport airport = airportRepository.findByAirportCode(code).orElseThrow(AirportDoesntExistException::new);
        return mapAirportToAirportResponse(airport);
    }

    private void checkIfAirportAlreadyExistByNameOrCodeAndThrowException(Airport airport) {
        if (!airportRepository.findByAirportName(airport.getAirportName()).isEmpty() ||
                !airportRepository.findByAirportCode(airport.getAirportCode()).isEmpty()){
            throw new AirportAlreadyExistException();
        }
    }

    private AirportResponse calculateMostPassengerAndReturnAirport(List<AirportResponse> airportResponses){
        double max = 0.0;
        AirportResponse result = airportResponses.get(0);
        for (AirportResponse airportResponse : airportResponses){
            if (airportResponse.getNumberOfPassengerAnnually() > max){
                max = airportResponse.getNumberOfPassengerAnnually();
                result = airportResponse;
            }
        }
        return result;
    }
}
