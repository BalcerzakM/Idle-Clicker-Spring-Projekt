package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @Column(nullable = false)
    private String title;

    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestTier questTier;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestType questType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private OpponentEntity opponent;

    @NotNull
    @Column(nullable = false)
    private String imagePath;

    public int calculateMoneyReward(CharacterEntity character) {
        Random random = new Random();
        return this.getQuestTier().getMultiplier() * character.getAuraLvl() + random.nextInt(character.getLuck() + 1); // tu wszędzie trzeba dodać walidacje, czy nie jest zerem
    }
    public int calculateAuraReward(CharacterEntity character) {
        return this.getQuestTier().getMultiplier() * character.getAuraLvl();
    }

}
