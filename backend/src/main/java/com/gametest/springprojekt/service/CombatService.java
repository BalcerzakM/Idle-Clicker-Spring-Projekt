package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.BackpackIsAlreadyFullException;
import com.gametest.springprojekt.exception.NoActiveQuestException;
import com.gametest.springprojekt.exception.QuestNotFoundException;
import com.gametest.springprojekt.exception.QuestStillActiveException;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import com.gametest.springprojekt.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CombatService {
    private final ItemTokenService itemTokenService;
    private final QuestRepository questRepository;
    private final CalculationService calculationService;

    @Transactional
    public CombatDto startCombat(CharacterEntity character) {

        ActiveQuestEntity activeQuest = character.getActiveQuest();

        if (character.getActiveQuest() == null){
            throw new NoActiveQuestException("Gracz nie wybrał żadnego questa");
        }

        if (character.getBackpack().size() >= character.getMAX_BACKPACK_SLOTS()) {
            throw new BackpackIsAlreadyFullException("Twój plecak jest pełny! Zrób w nim miejsce, zanim ruszysz do walki.");
        }

        if (Instant.now().isBefore(activeQuest.getEndTime())) {
            throw new QuestStillActiveException();
        }

        OpponentEntity opponent = activeQuest.getOpponent();

        Map<String, Integer> stats = character.getEquipmentStatsSum();

        int opponentHp = calculationService.calculateOpponentStat(character, opponent.getBaseEndurance(), activeQuest.getQuestTier());
        int characterHp = stats.get("endurance");

        List<Integer> combatLog;

        String questType;
        if (activeQuest.getQuestType() == QuestType.RIZZ_FIGHT) {
            combatLog = simulateRizzCombat(character, opponent, activeQuest.getQuestTier());
            questType = QuestType.RIZZ_FIGHT.toString();
        } else if (activeQuest.getQuestType() == QuestType.STRENGTH_FIGHT) {
            combatLog = simulateStrengthCombat(character, opponent, activeQuest.getQuestTier());
            questType = QuestType.STRENGTH_FIGHT.toString();
        } else {
            throw new IllegalArgumentException("Błędny typ questa!");
        }

        String enemyName = opponent.getName();
        String enemyImagePath = opponent.getImagePath();
        boolean playerWon = combatLog.size() % 2 != 0;

        int bonusAura = 0;
        int bonusMoney = 0;
        ItemDto rewardItemDto = null;

        if(playerWon) {
            bonusAura = activeQuest.getBonusAura();
            bonusMoney = activeQuest.getBonusMoney();

            ItemEntity rewardItem = itemTokenService.handleRewardToken(false);

            if (rewardItem != null) {
                rewardItemDto = rewardItem.generateItemDto();
            }

            character.grantQuestReward(bonusAura, bonusMoney, rewardItem);
        }

        character.setActiveQuest(null);

        return new CombatDto(
                combatLog,
                playerWon,
                characterHp,
                opponentHp,
                enemyName,
                enemyImagePath,
                questType,
                bonusMoney,
                bonusAura,
                rewardItemDto
        );
    }

    /*
        WALKA Z BOSSEM TO WERSJA BETA JESZCZE DO POPRAWY
    */
    @Transactional
    public CombatDto startBossCombat(CharacterEntity character) {
        if (character.getBackpack().size() >= character.getMAX_BACKPACK_SLOTS()) {
            throw new BackpackIsAlreadyFullException("Twój plecak jest pełny! Zrób w nim miejsce, zanim ruszysz do walki.");
        }

        List<QuestEntity> bossQuests = questRepository.findByQuestTierOrderByIdAsc(QuestTier.BOSS);

        int currentCharacterBoss = character.getCurrentBoss();

        if (currentCharacterBoss > bossQuests.size()) {
            throw new QuestNotFoundException("Brak zadań specjalnych.");
        }

        QuestEntity bossQuest = bossQuests.get(currentCharacterBoss - 1);

        OpponentEntity opponent = bossQuest.getOpponent();
        int opponentHp = calculationService.calculateOpponentStat(character, opponent.getBaseEndurance(), bossQuest.getQuestTier());

        Map<String, Integer> stats = character.getEquipmentStatsSum();
        int characterHp = stats.get("endurance");

        List<Integer> combatLog;

        if (bossQuest.getQuestType() == QuestType.RIZZ_FIGHT) {
            combatLog =  simulateRizzCombat(character, opponent, bossQuest.getQuestTier());

        } else if (bossQuest.getQuestType() == QuestType.STRENGTH_FIGHT) {
            combatLog = simulateStrengthCombat(character, opponent, bossQuest.getQuestTier());

        } else {
            throw new IllegalArgumentException("Błędny typ questa!");
        }

        String enemyName = opponent.getName();
        String enemyImagePath = opponent.getImagePath();
        boolean playerWon = combatLog.size() % 2 != 0;

        int bonusAura = 0;
        int bonusMoney = 0;
        ItemDto rewardItemDto = null;

        if(playerWon) {
            bonusAura = calculationService.calculateQuestAuraReward(character, bossQuest);
            bonusMoney = calculationService.calculateQuestMoneyReward(character, bossQuest);

            ItemEntity rewardItem = itemTokenService.handleRewardToken(true);

            if (rewardItem != null) {
                rewardItemDto = rewardItem.generateItemDto();
            }

            character.grantQuestReward(bonusAura, bonusMoney, rewardItem);
            character.setCurrentBoss(character.getCurrentBoss() + 1);
        }

        return new CombatDto(
                combatLog,
                playerWon,
                characterHp,
                opponentHp,
                enemyName,
                enemyImagePath,
                bossQuest.getQuestType().toString(),
                bonusMoney,
                bonusAura,
                rewardItemDto
        );
    }


    private List<Integer> simulateRizzCombat(CharacterEntity character, OpponentEntity opponent, QuestTier questTier) {
        Map<String, Integer> characterStats = character.getEquipmentStatsSum();

        int characterHp = characterStats.get("endurance");
        int characterRizz = characterStats.get("rizz");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int opponentHp = calculationService.calculateOpponentStat(character, opponent.getBaseEndurance(), questTier);
        int opponentRizz = calculationService.calculateOpponentStat(character, opponent.getBaseRizz(), questTier);
        int opponentAgility = calculationService.calculateOpponentStat(character, opponent.getBaseAgility(), questTier);
        int opponentLuck = calculationService.calculateOpponentStat(character, opponent.getBaseLuck(), questTier);

        int baseCharacterDmg = calculationService.calculateRizzFightBaseDamage(characterRizz, characterAgility, opponentAgility);
        int baseOpponentDmg = calculationService.calculateRizzFightBaseDamage(opponentRizz,opponentAgility, characterAgility);

        List<Integer> combatLog = new ArrayList<>();
        boolean playersAtack = true;
        while (characterHp > 0 && opponentHp > 0) {
            if(playersAtack) {
                int dmg = calculationService.calculateDamage(baseCharacterDmg, characterLuck);
                combatLog.add(dmg);
                opponentHp -= dmg;
                playersAtack = false;
            } else {
                int dmg = calculationService.calculateDamage(baseOpponentDmg, opponentLuck);
                combatLog.add(dmg);
                characterHp -= dmg;
                playersAtack = true;
            }
        }
        return combatLog;
    }

    private List<Integer> simulateStrengthCombat(CharacterEntity character, OpponentEntity opponent, QuestTier questTier) {
        Map<String, Integer> characterStats = character.getEquipmentStatsSum();

        int characterHp = characterStats.get("endurance");
        int characterStrength = characterStats.get("strength");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int opponentHp = calculationService.calculateOpponentStat(character, opponent.getBaseEndurance(), questTier);
        int opponentStrength = calculationService.calculateOpponentStat(character, opponent.getBaseStrength(), questTier);
        int opponentAgility = calculationService.calculateOpponentStat(character, opponent.getBaseAgility(), questTier);
        int opponentLuck = calculationService.calculateOpponentStat(character, opponent.getBaseLuck(), questTier);

        List<Integer> combatLog = new ArrayList<>();
        boolean playersAtack = true;
        while (characterHp > 0 && opponentHp > 0) {
            if(playersAtack) {
                if(calculationService.didDodge(opponentAgility)) {
                    combatLog.add(0);
                } else {
                    int dmg = calculationService.calculateDamage(characterStrength, characterLuck);
                    combatLog.add(dmg);
                    opponentHp -= dmg;
                }
                playersAtack = false;
            } else {
                if(calculationService.didDodge(characterAgility)) {
                    combatLog.add(0);
                } else {
                    int dmg = calculationService.calculateDamage(opponentStrength, opponentLuck);
                    combatLog.add(dmg);
                    characterHp -= dmg;
                }
                playersAtack = true;
            }
        }
        return combatLog;
    }





}
