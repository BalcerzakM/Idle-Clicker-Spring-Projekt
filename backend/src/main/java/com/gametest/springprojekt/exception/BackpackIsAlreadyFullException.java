package com.gametest.springprojekt.exception;

public class BackpackIsAlreadyFullException extends RuntimeException {
    public BackpackIsAlreadyFullException(String message) {
        super(message);
    }
}
