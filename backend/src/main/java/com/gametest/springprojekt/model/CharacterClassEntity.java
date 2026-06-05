package com.gametest.springprojekt.model;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String className;

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
    private int baseMoney;
}
