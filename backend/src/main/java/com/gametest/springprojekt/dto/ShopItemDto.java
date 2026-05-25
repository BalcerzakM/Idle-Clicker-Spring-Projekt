package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopItemDto {
    private Long shopOfferId;

    private String itemName;
    private String itemDescription;
    private SlotType slotType;

    private int totalRizz;
    private int totalStrength;
    private int totalAgility;
    private int totalEndurance;
    private int totalLuck;

    private int price;

    private String imagePath;
}
