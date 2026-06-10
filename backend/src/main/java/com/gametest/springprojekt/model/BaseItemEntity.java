package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank
    @Column(nullable = false, unique = true)
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

    @PositiveOrZero
    @Column(nullable = false)
    private int baseRizz;

    @PositiveOrZero
    @Column(nullable = false)
    private int baseStrength;

    @PositiveOrZero
    @Column(nullable = false)
    private int baseAgility;

    @PositiveOrZero
    @Column(nullable = false)
    private int baseEndurance;

    @PositiveOrZero
    @Column(nullable = false)
    private int baseLuck;

    @PositiveOrZero
    @Column(nullable = false)
    private int basePrice;

    @NotBlank
    @Column(nullable = false)
    private String imagePath;
}
