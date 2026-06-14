package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveQuestEntity {//osobna encja, bo potrzeba przechowywać nagrody, oponenta i czas zakończenia questa

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private Instant startTime;

    @NotNull
    @Column(nullable = false)
    private Instant endTime;

    @NotNull
    @Column(nullable = false)
    private String imagePath;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private OpponentEntity opponent;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestType questType;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestTier questTier;

    @PositiveOrZero
    @Column(nullable = false)
    private int bonusMoney;

    @PositiveOrZero
    @Column(nullable = false)
    private int bonusAura;

}
