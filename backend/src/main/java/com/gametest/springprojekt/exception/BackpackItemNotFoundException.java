package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BackpackItemNotFoundException extends RuntimeException {
    public BackpackItemNotFoundException(String message) {
        super(message);
    }
}
