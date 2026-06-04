package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoActiveQuestException extends RuntimeException {
    public NoActiveQuestException(String message) {
        super("Gracz nie wybrał zadania");
    }
}
