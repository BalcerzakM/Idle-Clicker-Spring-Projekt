package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.QuestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpecialQuestDto {
    private String questTitle;

    private String questDescription;

    private QuestType questType;

    private String opponentName;

    private String opponentImagePath;
}
