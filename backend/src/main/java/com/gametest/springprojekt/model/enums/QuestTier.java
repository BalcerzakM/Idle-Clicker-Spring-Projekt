package com.gametest.springprojekt.model.enums;

import lombok.Getter;

@Getter
public enum QuestTier {
    EASY(30),
    MEDIUM(45),
    HARD(60);

    private final int multiplier;

    QuestTier(int multiplier) {
        this.multiplier = multiplier;
    }
}
