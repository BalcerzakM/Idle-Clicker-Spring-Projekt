package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortCharacterInfoDto {
    private int money;
    private int cristals;
    private String avatarPicture;

    private int aura;
    private int auraLevel;
    private int nextLevelAuraRequirement;
    private int levelProgressPercent;
    private String characterClass;
}
