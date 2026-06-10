package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private CharacterEntity character;

    @NotBlank
    @Column(nullable = false)
    private String packageCode;

    @Positive
    @Column(nullable = false)
    private int paymentAmountInGrosze;

    @Positive
    @Column(nullable = false)
    private int cristalsAmount;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
