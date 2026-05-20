package com.gametest.springprojekt.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private OpponentEntity opponent;

    private Instant endTime;

    private String imagePath;

    private int bonusMoney;

    private int bonusAura;

}
