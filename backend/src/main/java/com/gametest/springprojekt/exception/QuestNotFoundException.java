package com.gametest.springprojekt.exception;

public class QuestNotFoundException extends RuntimeException {
    public QuestNotFoundException(String message) {
        super(message);
    }
}
