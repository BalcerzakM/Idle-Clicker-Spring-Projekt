package com.gametest.springprojekt.exception;

public class NotEnoughAvailableBaseItemsException extends RuntimeException {
    public NotEnoughAvailableBaseItemsException(String message) {
        super(message);
    }
}
