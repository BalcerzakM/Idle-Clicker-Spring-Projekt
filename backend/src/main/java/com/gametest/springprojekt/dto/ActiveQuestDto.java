package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ActiveQuestDto {
    private String questTitle;
    private Instant questStartTime;
    private Instant questEndTime;
    private String imagePath;
}
