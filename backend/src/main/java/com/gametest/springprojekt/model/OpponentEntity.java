package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

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
    private String imagePath;
}
