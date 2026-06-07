package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @NotNull
    @Column(nullable = false)
    private int baseRizz;

    @NotNull
    @Column(nullable = false)
    private int baseStrength;

    @NotNull
    @Column(nullable = false)
    private int baseAgility;

    @NotNull
    @Column(nullable = false)
    private int baseEndurance;

    @NotNull
    @Column(nullable = false)
    private int baseLuck;

    @NotNull
    @Column(nullable = false)
    private int basePrice;

    @NotNull
    @Column(nullable = false)
    private String imagePath;
}
