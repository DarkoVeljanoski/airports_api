package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AirportAlreadyExistException extends RuntimeException {

    private final static String MESSAGE = "Airport with that name already exist!";

    public AirportAlreadyExistException(){
        super(MESSAGE);
    }
}
