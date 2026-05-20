package com.gametest.springprojekt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuestStillActiveException extends RuntimeException {
    public QuestStillActiveException() {
        super("Quest nie został jeszcze ukończony!");
    }
}
