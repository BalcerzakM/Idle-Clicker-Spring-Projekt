package com.gametest.springprojekt.model.enums;

import lombok.Getter;

@Getter
public enum QuestTier {
    EASY(5),
    MEDIUM(10),
    HARD(15);

    private final int multiplier;

    QuestTier(int multiplier) {
        this.multiplier = multiplier;
    }
}
