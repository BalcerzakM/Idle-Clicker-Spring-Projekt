package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.BackpackItem;
import com.gametest.springprojekt.model.EquipmentItem;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;
import java.util.Set;

//przedmioty plus statystyki postaci
@Data
@AllArgsConstructor
public class ItemsAndStatsDto {
    private String name;
    private int auraLvl;
    private int aura;
    private int totalRizz;
    private int totalStrength;
    private int totalAgility;
    private int totalEndurance;
    private int totalLuck;

    private Set<EquipmentItem> equipment;

    private List<BackpackItem> backpack;

}
