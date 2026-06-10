package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OpponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

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

    @NotNull
    @Column(nullable = false)
    private String imagePath;
}
