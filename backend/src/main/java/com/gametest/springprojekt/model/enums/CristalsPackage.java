package com.gametest.springprojekt.model.enums;

import lombok.Getter;

@Getter
public enum CristalsPackage {
    SMALL(9.99, 50),
    BASIC(19.99, 120),
    MEDIUM(49.99, 350),
    LARGE(99.99, 800),
    HUGE(199.99, 1800),
    ENORMOUS(499.99, 5000);

    private final double price;
    private final int cristals;


    CristalsPackage(double price, int cristals) {
        this.price = price;
        this.cristals = cristals;
    }
}
