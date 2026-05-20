package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.exception.NoActiveQuest;
import com.gametest.springprojekt.model.ActiveQuestEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final Random random = new Random();
    private final CharacterRepository characterRepository; // Intelij nie wie, że metoda setActiveQuest modyfikuje też repo

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

        int aura = character.getAura();

        int questTierVariable = quest.getQuestTier().getMultiplier(); //multiplayer z enuma


        long questDuration = calculateQuestDuration(character, quest);

        int moneyReward = quest.calculateMoneyReward(character);

        int auraReward = quest.calculateAuraReward(character);

        //ItemEntity itemReward = null;

        return new QuestDto(
                quest.getId(),
                quest.getTitle(),
                quest.getDescription(),
                quest.getQuestTier(),
                quest.getQuestType(),
                quest.getOpponent().getName(),
                questDuration, moneyReward, auraReward

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

    /**
     * zwraca aktualnego questa wraz z policzonym czasem zakończenia
     * @param character
     * @return
     * @throws NoActiveQuest
     */

    public ActiveQuestDto getActiveQuestDto(CharacterEntity character) throws NoActiveQuest {

        if (character.getActiveQuest() == null){
            throw new NoActiveQuest("Gracz nie wybrał żadnego questa");
        }
        ActiveQuestEntity aq = character.getActiveQuest();

        ActiveQuestDto activeQuest = new ActiveQuestDto(aq.getTitle(), aq.getEndTime() , aq.getImagePath() );

        return activeQuest;
    }

    private Instant calculateQuestEndTime(long questDuration) {
        return Instant.now().plusSeconds(questDuration);
    }

    private static long calculateQuestDuration(CharacterEntity character,QuestEntity quest) {
        int aura = character.getAura();
        int questTierVariable = quest.getQuestTier().getMultiplier();
        return (long) questTierVariable*aura;
    }

    @Transactional //fajne to transactional bo i gwarantuje atomowość i nie trzeba do repo.save robić, w tym przypadku do characterRepo
    public ActiveQuestDto setActiveQuest(CharacterEntity character, Long questId) {
        QuestEntity quest = questRepository.findById(questId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono questa"));

        Instant qEndTime = calculateQuestEndTime(calculateQuestDuration(character,quest));

        int bonusMoney = quest.calculateMoneyReward(character);
        int bonusAura = quest.calculateAuraReward(character);

        character.setActiveQuest(new ActiveQuestEntity(null, quest.getTitle(),quest.getOpponent(),qEndTime, quest.getImagePath(),bonusMoney,bonusAura));

        return new ActiveQuestDto(quest.getTitle(), qEndTime , quest.getImagePath());// zwracamy Dto a nie encje bo api nie musi wiedzieć o przeciwniku
    }
}
