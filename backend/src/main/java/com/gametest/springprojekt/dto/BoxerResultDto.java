package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoxerResultDto {
    private int result;
    private int winAmount;
    private int bet;
    private boolean isLucky;
}
