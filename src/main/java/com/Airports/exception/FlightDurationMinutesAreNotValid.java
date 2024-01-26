package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FlightDurationMinutesAreNotValid extends RuntimeException{

    private static final String MESSAGE = "Flight Duration Minutes Are Not Valid";

    public FlightDurationMinutesAreNotValid(){
        super(MESSAGE);
    }
}
