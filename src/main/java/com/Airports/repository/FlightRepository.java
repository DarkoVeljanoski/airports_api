package com.Airports.repository;

import com.Airports.model.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findAllByCodeOfStartingAirportAndCodeOfDestinationAirport(String codeStart, String codeDestination);

    List<Flight> findAllByCodeOfStartingAirportOrderByCodeOfDestinationAirportAsc(String codeStart);


    List<Flight> findAllByCodeOfStartingAirportOrderByDepartureTimeInMinutesAsc(String codeStart);
}
