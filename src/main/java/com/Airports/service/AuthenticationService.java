package com.Airports.service;

import com.Airports.model.auth.AuthenticationRequest;
import com.Airports.model.auth.AuthenticationResponse;
import com.Airports.model.auth.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
