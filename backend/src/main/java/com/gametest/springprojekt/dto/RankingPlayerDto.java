package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RankingPlayerDto {
    private String characterName;
    private String avatarPicture;
    private Map<SlotType, String> eqItemsPicrures;
    private int auraLvl;
    private int totalRizz;
    private int totalStrength;
    private int totalAgility;
    private int totalEndurance;
    private int totalLuck;
}
