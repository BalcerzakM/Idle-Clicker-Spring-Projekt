package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CharacterIsInAGangException extends RuntimeException {
    public CharacterIsInAGangException(String message) {
        super(message);
    }
    }
