package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BouncerDutyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Instant bouncerDutyStartTime;

    @NotNull
    @Column(nullable = false)
    private Instant bouncerDutyEndTime;

    @PositiveOrZero
    @Column(nullable = false)
    private int reward;
}
