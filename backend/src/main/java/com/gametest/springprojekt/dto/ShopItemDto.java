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

    private int bonusRizz;
    private int bonusStrength;
    private int bonusAgility;
    private int bonusEndurance;
    private int bonusLuck;

    private int price;

    private String imagePath;
}
