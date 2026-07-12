package com.gametest.springprojekt.exception;

public class InsufficientVotesException extends RuntimeException {
    public InsufficientVotesException(String message) {
        super(message);
    }
}
