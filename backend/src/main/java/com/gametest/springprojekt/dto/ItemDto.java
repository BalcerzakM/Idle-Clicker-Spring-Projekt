package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.SlotType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {

    //w roznych kontekstach jest czyms troche innym, np w sklepie jest ID oferty, w plecaku jest ID itemu w plecaku itd.
    private Long id;

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
