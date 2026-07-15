package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterInBattleDto {
    private String characterName;
    private String imagePath;
    private int hp;
}
