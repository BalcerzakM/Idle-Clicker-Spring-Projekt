package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
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
    private Random random = new Random();

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    //pobiera losowe 3 questy po jednym z kazdego tieru
    public List<QuestEntity> getRandomQuestList() {
        List<QuestEntity> randomQuestList = new ArrayList<>();

        List<QuestEntity> questList = questRepository.findByQuestTier(QuestTier.EASY);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        questList = questRepository.findByQuestTier(QuestTier.MEDIUM);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        questList = questRepository.findByQuestTier(QuestTier.HARD);
        randomQuestList.add(questList.get(random.nextInt(questList.size())));

        return randomQuestList;
    }

    //generuje dto z parametrami questa na podstawie expa postaci
    private QuestDto generateQuestDto(QuestEntity quest, CharacterEntity character) {
        //NIE MA JESZCZE AURY WIEC TYMCZASOWO NA SZTYWNO
        int aura = 1; //character.getAura;

        int questTierVariable = quest.getQuestTier().getMultiplier();

        //tutaj jakies algorytmy sie wykmini
        //questTime raczej w sekundach
        int questTime = questTierVariable*aura;

        int moneyReward = questTierVariable*aura + random.nextInt(aura%character.getLuck());

        int auraReward = questTierVariable*aura;
        //ItemEntity itemReward = null;

        return new QuestDto(
                quest.getId(),
                quest.getTitle(),
                quest.getDescription(),
                quest.getQuestTier(),
                quest.getQuestType(),
                quest.getOpponent().getName(),
                questTime, moneyReward, auraReward//, itemReward
        );
    }

    //generuje dto i robi liste dto do controllera
    public List<QuestDto> generateQuestDtoList(List<QuestEntity> questList, CharacterEntity character) {
        List<QuestDto> questDtoList = new ArrayList<>();

        for (QuestEntity quest : questList) {
            questDtoList.add(generateQuestDto(quest, character));
        }

        return questDtoList;
    }
}
