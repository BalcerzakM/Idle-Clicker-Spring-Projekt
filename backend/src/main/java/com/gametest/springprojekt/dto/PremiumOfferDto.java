package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.CristalsPackage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PremiumOfferDto {
    private String packageCode;
    private double price;
    private int cristals;
}
