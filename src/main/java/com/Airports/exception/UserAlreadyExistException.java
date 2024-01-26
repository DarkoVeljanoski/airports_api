package com.Airports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException {

    private final static String MESSAGE = "User with that username already exist!";

    public UserAlreadyExistException(){
        super(MESSAGE);
    }
}
