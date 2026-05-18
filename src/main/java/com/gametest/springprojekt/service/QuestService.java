package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.exception.NoActiveQuest;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class QuestService {

    private QuestRepository questRepository;
    private Random random = new Random();
    private CharacterRepository characterRepository;

    public QuestService(QuestRepository questRepository, CharacterRepository characterRepository) {
        this.questRepository = questRepository;
        this.characterRepository = characterRepository;
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

        int questTierVariable = quest.getQuestTier().getMultiplier(); //multiplayer z enuma

        //tutaj jakies algorytmy sie wykmini
        //questTime raczej w sekundach
        //liczenie questTimu fajnie aby było jako osobna metoda bo to często będzie się przewijało
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
                questTime, moneyReward, auraReward

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

    public ActiveQuestDto getActiveQuestDto(CharacterEntity character) throws NoActiveQuest {


        if (character.getActiveQuest() == null){
            throw new NoActiveQuest("Gracz nie wybrał żadnego questa");
        }
        QuestEntity aq = character.getActiveQuest();


        Instant qEndTime = calculateQuestEndTime(this.calculateQuestDuration(character,aq));

        ActiveQuestDto activeQuest = new ActiveQuestDto(aq.getTitle(), qEndTime , aq.getImagePath() );

        return activeQuest;
    }

    private Instant calculateQuestEndTime(long questDuration) {
        return Instant.now().plusSeconds(questDuration);

    }

    private long calculateQuestDuration(CharacterEntity character,QuestEntity quest) {
        int aura = character.getAura();
        int questTierVariable = quest.getQuestTier().getMultiplier();
        long questDuration = questTierVariable*aura;
        return questDuration;
    }

}
