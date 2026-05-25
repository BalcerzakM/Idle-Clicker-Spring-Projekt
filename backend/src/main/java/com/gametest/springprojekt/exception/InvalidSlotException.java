package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class InvalidSlotException extends RuntimeException {
    public InvalidSlotException(String message) {
        super(message);
    }
}
