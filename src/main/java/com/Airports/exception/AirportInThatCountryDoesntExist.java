package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AirportInThatCountryDoesntExist extends RuntimeException{

    private final static String MESSAGE = "Airport in that country doesnt exist!";

    public AirportInThatCountryDoesntExist(){
        super(MESSAGE);
    }
}
