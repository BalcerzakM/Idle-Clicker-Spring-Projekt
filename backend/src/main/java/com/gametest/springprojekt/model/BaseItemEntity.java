package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Column(nullable = false)
    private int baseRizz;

    @Column(nullable = false)
    private int baseStrength;

    @Column(nullable = false)
    private int baseAgility;

    @Column(nullable = false)
    private int baseEndurance;

    @Column(nullable = false)
    private int baseLuck;

    @Column(nullable = false)
    private int basePrice;

    @Column(nullable = false)
    private String imagePath;
}
