package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AirportWithThatNameDoesntExistException extends RuntimeException {

    private static final String MESSAGE = "Airport with that name doesnt exist!";

    public AirportWithThatNameDoesntExistException(){
        super(MESSAGE);
    }
}
