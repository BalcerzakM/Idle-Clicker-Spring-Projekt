package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CombatDto {
    private List<String> combatSequence;

    private int moneyReward;

    private int auraReward;

    private ItemDto itemReward;
}
