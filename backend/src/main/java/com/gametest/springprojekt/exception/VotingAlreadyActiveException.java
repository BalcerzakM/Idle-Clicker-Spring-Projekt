package com.gametest.springprojekt.exception;

public class VotingAlreadyActiveException extends RuntimeException {
    public VotingAlreadyActiveException(String message) {
        super(message);
    }
}
