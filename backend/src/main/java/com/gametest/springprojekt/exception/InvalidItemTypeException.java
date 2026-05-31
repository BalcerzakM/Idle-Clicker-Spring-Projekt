package com.gametest.springprojekt.exception;

public class InvalidItemTypeException extends RuntimeException {
    public InvalidItemTypeException(String message) {
        super(message);
    }
}
