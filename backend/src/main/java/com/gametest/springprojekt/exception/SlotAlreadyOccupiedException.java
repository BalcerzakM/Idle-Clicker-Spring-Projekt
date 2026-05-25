package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)

public class SlotAlreadyOccupiedException extends RuntimeException {
    public SlotAlreadyOccupiedException(String message) {
        super(message);
    }
}
