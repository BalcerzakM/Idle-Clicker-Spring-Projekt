package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.dto.QuestDto;
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
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CombatService {
    private final ItemTokenService itemTokenService;
    private final Random random = new Random();
    private final QuestRepository questRepository;

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

        int opponentHp = opponent.getBaseEndurance() * character.getAuraLvl();
        int characterHp = stats.get("endurance");

        List<Integer> combatLog;

        String questType;
        if (activeQuest.getQuestType() == QuestType.RIZZ_FIGHT) {
            combatLog =  simulateRizzCombat(character, opponent, true);
            questType = QuestType.RIZZ_FIGHT.toString();
        } else if (activeQuest.getQuestType() == QuestType.STRENGTH_FIGHT) {
            combatLog = simulateStrengthCombat(character, opponent, true);
            questType = QuestType.STRENGTH_FIGHT.toString();
        } else {
            throw new IllegalArgumentException("Błędny typ questa!");
        }

        String enemyName = opponent.getName();
        String enemyImagePath = opponent.getImagePath();
        boolean playerWon = combatLog.size() % 2 != 0;

        int bonusAura;
        int bonusMoney;
        ItemEntity rewardItem;
        ItemDto rewardItemDto;

        if(playerWon) {
            bonusAura = activeQuest.getBonusAura();
            bonusMoney = activeQuest.getBonusMoney();
            rewardItem = itemTokenService.handleRewardToken();

        } else {
            bonusAura = 0;
            bonusMoney = 0;
            rewardItem = null;

        }

        if (rewardItem != null) {
            rewardItemDto = rewardItem.generateItemDto();

        } else {
            rewardItemDto = null;
        }

        character.grantQuestReward(bonusAura, bonusMoney, rewardItem);

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

    @Transactional
    public CombatDto startBossCombat(CharacterEntity character) {
        if (character.getBackpack().size() >= character.getMAX_BACKPACK_SLOTS()) {
            throw new BackpackIsAlreadyFullException("Twój plecak jest pełny! Zrób w nim miejsce, zanim ruszysz do walki.");
        }

        List<QuestEntity> bossQuests = questRepository.findByQuestTier(QuestTier.BOSS);

        int currentCharacterBoss = character.getCurrentBoss();

        if (currentCharacterBoss >= bossQuests.size()) {
            throw new QuestNotFoundException("Pokonałeś już wszystkich bossów!");
        }

        QuestEntity bossQuest = bossQuests.get(currentCharacterBoss);

        OpponentEntity opponent = bossQuest.getOpponent();
        int opponentHp = opponent.getBaseEndurance();

        Map<String, Integer> stats = character.getEquipmentStatsSum();
        int characterHp = stats.get("endurance");

        List<Integer> combatLog;

        if (bossQuest.getQuestType() == QuestType.RIZZ_FIGHT) {
            combatLog =  simulateRizzCombat(character, opponent, false);

        } else if (bossQuest.getQuestType() == QuestType.STRENGTH_FIGHT) {
            combatLog = simulateStrengthCombat(character, opponent, false);

        } else {
            throw new IllegalArgumentException("Błędny typ questa!");
        }

        String enemyName = opponent.getName();
        String enemyImagePath = opponent.getImagePath();
        boolean playerWon = combatLog.size() % 2 != 0;

        int bonusAura = 0;
        int bonusMoney = 0;
        ItemEntity rewardItem = null;
        ItemDto rewardItemDto = null;

        if(playerWon) {
            bonusAura = bossQuest.calculateAuraReward(character);
            bonusMoney = bossQuest.calculateMoneyReward(character);

            rewardItem = itemTokenService.handleRewardToken();

            if (rewardItem != null) {
                rewardItemDto = rewardItem.generateItemDto();
            }

            character.grantQuestReward(bonusAura, bonusMoney, rewardItem);
            character.setCurrentBoss(currentCharacterBoss + 1);
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


    private List<Integer> simulateRizzCombat(CharacterEntity character, OpponentEntity opponent, Boolean scaling) {
        int scalingValue = character.getAuraLvl();
        if (!scaling) {
            scalingValue = 1;
        }

        Map<String, Integer> characterStats = character.getEquipmentStatsSum();
        int characterHp = characterStats.get("endurance");
        int characterRizz = characterStats.get("rizz");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int baseCharacterDmg = (int) ((characterRizz + (0.5 * characterAgility)));


        int opponentHp = opponent.getBaseEndurance() * scalingValue;
        int opponentRizz = opponent.getBaseRizz() * scalingValue;
        int opponentAgility = opponent.getBaseAgility() * scalingValue;
        int opponentLuck = opponent.getBaseLuck() * scalingValue;

        int baseOpponentDmg = (int) (opponentRizz + (0.5 * opponentAgility));


        List<Integer> combatLog = new ArrayList<>();
        boolean playersAtack = true;
        while (characterHp > 0 && opponentHp > 0) {
            if(playersAtack) {
                int dmg = calculateDamage(baseCharacterDmg, characterLuck);
                combatLog.add(dmg);
                opponentHp -= dmg;
                playersAtack = false;
            } else {
                int dmg = calculateDamage(baseOpponentDmg, opponentLuck);
                combatLog.add(dmg);
                characterHp -= dmg;
                playersAtack = true;
            }
        }
        return combatLog;
    }

    private List<Integer> simulateStrengthCombat(CharacterEntity character, OpponentEntity opponent, Boolean scaling) {
        int scalingValue = character.getAuraLvl();
        if (!scaling) {
            scalingValue = 1;
        }

        Map<String, Integer> characterStats = character.getEquipmentStatsSum();
        int characterHp = characterStats.get("endurance");
        int characterStrength = characterStats.get("strength");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int opponentHp = opponent.getBaseEndurance() * scalingValue;
        int opponentStrength = opponent.getBaseStrength() * scalingValue;
        int opponentAgility = opponent.getBaseAgility() * scalingValue;
        int opponentLuck = opponent.getBaseLuck() * scalingValue;

        List<Integer> combatLog = new ArrayList<>();
        boolean playersAtack = true;
        while (characterHp > 0 && opponentHp > 0) {
            if(playersAtack) {
                if(random.nextInt(10000) + opponentAgility > 9000) {
                    combatLog.add(0);
                } else {
                    int dmg = calculateDamage(characterStrength, characterLuck);
                    combatLog.add(dmg);
                    opponentHp -= dmg;
                }
                playersAtack = false;
            } else {
                if(random.nextInt(10000) + characterAgility > 9000) {
                    combatLog.add(0);
                } else {
                    int dmg = calculateDamage(opponentStrength, opponentLuck);
                    combatLog.add(dmg);
                    characterHp -= dmg;
                }
                playersAtack = true;
            }
        }
        return combatLog;
    }

    private int calculateDamage(int baseDamage, int luck) {
        if (baseDamage <= 0) baseDamage = 1;

        int minDmg = (int) (baseDamage * 0.8);
        int maxDmg = (int) (baseDamage * 1.2);
        int actualDamage = random.nextInt(maxDmg - minDmg + 1) + minDmg;

        if(random.nextInt(10000) + luck > 9000) {
            actualDamage *= 2;
        }
        return Math.max(actualDamage, 1);
    }



}
