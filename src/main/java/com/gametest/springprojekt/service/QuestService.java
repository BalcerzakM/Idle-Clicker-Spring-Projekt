package com.gametest.springprojekt.service;

import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuestService {

    private QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public List<QuestEntity> getRandomQuestList() { //pobiera losowe 3 questy po jednym z kazdego tieru
        Random random = new Random();
        List<QuestEntity> randomQuestList = new ArrayList<>();

        List<QuestEntity> questList = questRepository.findByQuestTier(QuestTier.EASY);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        questList = questRepository.findByQuestTier(QuestTier.MEDIUM);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        questList = questRepository.findByQuestTier(QuestTier.HARD);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        return randomQuestList;
    }
}
