package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EffectDto {
    private ItemDto item;
    private Instant effectStartTime;
    private Instant effectEndTime;
}
