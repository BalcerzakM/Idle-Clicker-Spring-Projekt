package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class VehicleIsAlreadyRentedException extends RuntimeException {
    public VehicleIsAlreadyRentedException(String message) {
        super(message);
    }
}
