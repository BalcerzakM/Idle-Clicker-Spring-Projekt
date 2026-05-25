package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//ta klasa reprezentuje podstawowy, niewyskalowany jeszcze item

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BaseItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private SlotType slotType;
    private int baseRizz;
    private int baseStrength;
    private int baseAgility;
    private int baseEndurance;
    private int baseLuck;
    private int basePrice;
    private String imagePath;
}
