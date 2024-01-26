package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CodeOfStartingAirportIsNotValidException extends RuntimeException{

    private static final String MESSAGE = "Code of the starting airport is not valid!";
    public CodeOfStartingAirportIsNotValidException(){
        super(MESSAGE);
    }
}
