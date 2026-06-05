package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.exception.NoActiveQuestException;
import com.gametest.springprojekt.exception.QuestNotFoundException;
import com.gametest.springprojekt.model.ActiveQuestEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.QuestOfferEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestOfferRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final Random random = new Random();
    private final CharacterRepository characterRepository; // Intelij nie wie, że metoda setActiveQuest modyfikuje też repo
    private final QuestOfferRepository questOfferRepository;
    private final int QUEST_OFFER_SIZE = 3;

    public QuestService(QuestRepository questRepository, CharacterRepository characterRepository, QuestOfferRepository questOfferRepository) {
        this.questRepository = questRepository;
        this.characterRepository = characterRepository;
        this.questOfferRepository = questOfferRepository;
    }

    @Transactional
    public List<QuestDto> getQuests(CharacterEntity character) {
        List<QuestOfferEntity> questOffers = questOfferRepository.findByCharacter(character);

        List<QuestEntity> quests;

        if (questOffers.size() != QUEST_OFFER_SIZE) {
            questOfferRepository.deleteByCharacter(character);
            questOffers.clear();

            quests = getRandomQuestList();

            for (QuestEntity newQuest : quests) {
                questOffers.add(new QuestOfferEntity(null, newQuest, character));
            }
            questOfferRepository.saveAll(questOffers);
        }

        quests = questOffers.stream().map(QuestOfferEntity::getQuest).collect(Collectors.toList());
        return generateQuestDtoList(quests, character);
    }

    //pobiera losowe 3 questy po jednym z kazdego tieru
    private List<QuestEntity> getRandomQuestList() {
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
    private List<QuestDto> generateQuestDtoList(List<QuestEntity> questList, CharacterEntity character) {
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
     * @throws NoActiveQuestException
     */

    public ActiveQuestDto getActiveQuestDto(CharacterEntity character) throws NoActiveQuestException {

        if (character.getActiveQuest() == null){
            throw new NoActiveQuestException("Gracz nie wybrał żadnego questa");
        }
        ActiveQuestEntity aq = character.getActiveQuest();

        ActiveQuestDto activeQuest = new ActiveQuestDto(aq.getTitle(), aq.getEndTime() , aq.getImagePath() );

        return activeQuest;
    }

    private Instant calculateQuestEndTime(long questDuration) {
        return Instant.now().plusSeconds(questDuration);
    }

    private static long calculateQuestDuration(CharacterEntity character,QuestEntity quest) {
        int aura = character.getAuraLvl();
        int questTierVariable = quest.getQuestTier().getMultiplier();
        return (long) questTierVariable*aura;
    }

    @Transactional //fajne to transactional bo i gwarantuje atomowość i nie trzeba do repo.save robić, w tym przypadku do characterRepo
    public ActiveQuestDto setActiveQuest(CharacterEntity character, Long questId) {
        QuestEntity quest = questRepository.findById(questId)
                .orElseThrow(() -> new QuestNotFoundException("Nie znaleziono questa"));

        QuestOfferEntity questOffer = questOfferRepository.findByQuestAndCharacter(quest, character);
        if (questOffer == null) {
            throw new QuestNotFoundException("Nie znaleziono questa");
        }
        questOfferRepository.delete(questOffer);

        Instant qEndTime = calculateQuestEndTime(calculateQuestDuration(character,quest));

        int bonusMoney = quest.calculateMoneyReward(character);
        int bonusAura = quest.calculateAuraReward(character);

        character.setActiveQuest(new ActiveQuestEntity(null, quest.getTitle(),quest.getOpponent(),qEndTime, quest.getImagePath(),bonusMoney,bonusAura));

        return new ActiveQuestDto(quest.getTitle(), qEndTime , quest.getImagePath());// zwracamy Dto a nie encje bo api nie musi wiedzieć o przeciwniku
    }
}
