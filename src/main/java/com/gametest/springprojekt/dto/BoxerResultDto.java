package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoxerResultDto {
    private int result;
    private int rizzChange;
    private int newRizz;
    private int newMoney;
    private boolean isLucky;
}
