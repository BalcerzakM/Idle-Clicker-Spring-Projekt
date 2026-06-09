package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.QuestType;
import jakarta.persistence.*;
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

    private String title;

    private Instant startTime;

    private Instant endTime;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private OpponentEntity opponent;

    @Enumerated(EnumType.STRING)
    private QuestType questType;

    private int bonusMoney;

    private int bonusAura;

}
