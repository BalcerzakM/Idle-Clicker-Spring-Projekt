package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class FullCharacterInfoDto {
    private String name;
    private String avatarPicture;
    private int auraLvl;
    private int aura;
    private int totalRizz;
    private int totalStrength;
    private int totalAgility;
    private int totalEndurance;
    private int totalLuck;

    private String vehicleName;
    private String vehicleImagePath;
    private int vehicleTimeReductionPercent;
    private String vehicleExpiryTime;

    private List<ItemDto> equipment;

    private List<ItemDto> backpack;

}

