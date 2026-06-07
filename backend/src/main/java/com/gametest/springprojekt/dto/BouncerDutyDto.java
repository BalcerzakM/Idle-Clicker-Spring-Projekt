package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BouncerDutyDto {
    private Instant dutyStartTime;
    private Instant dutyEndTime;
    private int reward;
}
