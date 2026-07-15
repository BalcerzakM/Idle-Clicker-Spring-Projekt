package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GangCombatDto {
    private List<Integer> combatLog;
    private boolean teamAWon;

    private String teamAImageFolder;
    private String teamBImageFolder;

    private List<CharacterInBattleDto> teamACharacters;
    private List<CharacterInBattleDto> teamBCharacters;

    private int moneyReward;
    private int auraReward;
    private ItemDto itemReward;
}