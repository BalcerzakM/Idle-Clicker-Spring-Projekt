package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RankingPositionDto {
    private long rank;
    private int page;
}
