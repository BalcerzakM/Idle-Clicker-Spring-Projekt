package com.gametest.springprojekt.exception;

public class InsufficientMoneyException extends RuntimeException {
    public InsufficientMoneyException(String message) {
        super("Gracz ma za mało złota!");
    }
}
