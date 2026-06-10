package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BaseVehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String imagePath;

    @Positive
    @Column(nullable = false)
    private int price;

    @Min(1)
    @Max(99)
    @Column(nullable = false)
    private int timeReductionPercent;
}
