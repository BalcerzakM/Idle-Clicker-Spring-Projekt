package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.dto.SpecialQuestDto;
import com.gametest.springprojekt.exception.NoActiveQuestException;
import com.gametest.springprojekt.exception.QuestNotFoundException;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestOfferRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final Random random = new Random();
    private final CharacterRepository characterRepository; // Intelij nie wie, że metoda setActiveQuest modyfikuje też repo
    private final QuestOfferRepository questOfferRepository;
    private final VehicleService vehicleService;
    private final CalculationService calculationService;
    private final int QUEST_OFFER_SIZE = 3;

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
        long questDuration = calculationService.calculateQuestDuration(character, quest);

        int moneyReward = calculationService.calculateQuestMoneyReward(character, quest);

        int auraReward = calculationService.calculateQuestAuraReward(character, quest);

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

        ActiveQuestDto activeQuest = new ActiveQuestDto(aq.getTitle(),aq.getStartTime(), aq.getEndTime() , aq.getImagePath() );

        return activeQuest;
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

        vehicleService.validateAndRemoveExpiredVehicle(character);

        Instant qEndTime = calculationService.calculateQuestEndTime(Instant.now(), calculationService.calculateQuestDuration(character,quest));
        Instant qStartTime = Instant.now();

        int bonusMoney = calculationService.calculateQuestMoneyReward(character, quest);
        int bonusAura = calculationService.calculateQuestAuraReward(character, quest);


        character.setActiveQuest(new ActiveQuestEntity(null, quest.getTitle(),qStartTime, qEndTime, quest.getImagePath(), quest.getOpponent(), quest.getQuestType(), quest.getQuestTier(), bonusMoney, bonusAura));

        return new ActiveQuestDto(quest.getTitle(),qStartTime, qEndTime , quest.getImagePath());// zwracamy Dto a nie encje bo api nie musi wiedzieć o przeciwniku
    }

    public SpecialQuestDto getCurrentBoss(CharacterEntity character) {
        List<QuestEntity> bossQuests = questRepository.findByQuestTierOrderByIdAsc(QuestTier.BOSS);

        int currentCharacterBoss = character.getCurrentBoss();

        if (currentCharacterBoss > bossQuests.size()) {
            throw new QuestNotFoundException("Brak zadań specjalnych.");
        }

        return generateSpecialQuestDto(bossQuests.get(currentCharacterBoss - 1));
    }

    private SpecialQuestDto generateSpecialQuestDto(QuestEntity bossQuest) {
        return new SpecialQuestDto(
                bossQuest.getTitle(),
                bossQuest.getDescription(),
                bossQuest.getQuestType(),
                bossQuest.getOpponent().getName(),
                bossQuest.getOpponent().getImagePath()
        );
    }
}
