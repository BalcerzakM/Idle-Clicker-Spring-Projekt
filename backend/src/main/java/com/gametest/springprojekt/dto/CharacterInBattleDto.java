package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInBattleDto {
    private String characterName;
    private String imagePath;
    private int hp;
}
