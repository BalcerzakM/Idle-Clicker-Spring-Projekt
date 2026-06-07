package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CombatDto {
    private List<Integer> combatLog;
    private boolean playerWon;
    private int playerHp;
    private int enemyHp;
    private String enemyName;
    private String enemyImagePath;
    private String questType;

    private int moneyReward;
    private int auraReward;
    private ItemDto itemReward;
}
