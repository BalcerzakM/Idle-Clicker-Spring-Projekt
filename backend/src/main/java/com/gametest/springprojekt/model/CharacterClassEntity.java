package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String className;

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
    private int baseMoney;
}
