package com.gametest.springprojekt.model.enums;

import lombok.Getter;

@Getter
public enum CristalsPackage {
    SMALL(999, 50),
    BASIC(1999, 120),
    MEDIUM(4999, 350),
    LARGE(9999, 800),
    HUGE(19999, 1800),
    ENORMOUS(49999, 5000);

    private final int priceInGrosze;
    private final int cristals;


    CristalsPackage(int priceInGrosze, int cristals) {
        this.priceInGrosze = priceInGrosze;
        this.cristals = cristals;
    }
}
