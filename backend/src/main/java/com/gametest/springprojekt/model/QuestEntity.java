package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class QuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestTier questTier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestType questType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    private OpponentEntity opponent;

    @Column(nullable = false)
    private String imagePath;

    public int calculateMoneyReward(CharacterEntity character) {
        Random random = new Random();
        int aura = character.getAuraLvl();
        return this.getQuestTier().getMultiplier() * aura + random.nextInt(aura%character.getLuck()); // tu wszędzie trzeba dodać walidacje, czy nie jest zerem
    }
    public int calculateAuraReward(CharacterEntity character) {
        return this.getQuestTier().getMultiplier() * character.getAuraLvl();
    }

}
