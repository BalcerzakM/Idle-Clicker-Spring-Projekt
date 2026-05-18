package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestDto {
    private long questId;

    private String questTitle;

    private String questDescription;

    private QuestTier questTier;

    private QuestType questType;

    private String opponentName;

    private int questTime;

    private int moneyReward;

    private int auraReward;

    private String imagePath;

//    /* mozliwosc dropniecia itemu, bedzie moglo byc nullem */
//    private ItemEntity itemReward;
}
