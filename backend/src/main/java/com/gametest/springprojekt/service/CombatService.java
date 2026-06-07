package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.BackpackIsAlreadyFullException;
import com.gametest.springprojekt.exception.NoActiveQuestException;
import com.gametest.springprojekt.exception.QuestStillActiveException;
import com.gametest.springprojekt.model.ActiveQuestEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.OpponentEntity;
import com.gametest.springprojekt.model.enums.QuestType;
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
        int opponentHp = opponent.getBaseEndurance() * character.getAuraLvl();
        int characterHp = character.getEndurance();
        List<Integer> combatLog;

        String questType;
        if (activeQuest.getQuestType() == QuestType.RIZZ_FIGHT) {
            combatLog =  simulateRizzCombat(character, opponent);
            questType = QuestType.RIZZ_FIGHT.toString();
        } else if (activeQuest.getQuestType() == QuestType.STRENGTH_FIGHT) {
            combatLog = simulateStrengthCombat(character, opponent);
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
        //String rewardItemImagePath;
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
            //rewardItemImagePath = rewardItem.getBaseItem().getImagePath();
            rewardItemDto = rewardItem.generateItemDto();
            character.addItemToBackpack(rewardItem);
        } else {
            rewardItemDto = null;
            //rewardItemImagePath = null;
        }

        character.setAura(character.getAura() + bonusAura);
        character.setMoney(character.getMoney() + bonusMoney);
        character.setActiveQuest(null);

        return new CombatDto(combatLog, playerWon, characterHp, opponentHp, enemyName, enemyImagePath, questType, bonusMoney, bonusAura, rewardItemDto);
    }

    private List<Integer> simulateRizzCombat(CharacterEntity character, OpponentEntity opponent) {

        int characterAuraLvl = character.getAuraLvl();

        Map<String, Integer> characterStats = character.getEquipmentStatsSum();
        int characterHp = characterStats.get("endurance");
        int characterRizz = characterStats.get("rizz");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int baseCharacterDmg = (int) ((characterRizz + (0.5 * characterAgility)));


        int opponentHp = opponent.getBaseEndurance() * characterAuraLvl;
        int opponentRizz = opponent.getBaseRizz() * characterAuraLvl;
        int opponentAgility = opponent.getBaseAgility() * characterAuraLvl;
        int opponentLuck = opponent.getBaseLuck() * characterAuraLvl;

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

    private List<Integer> simulateStrengthCombat(CharacterEntity character, OpponentEntity opponent) {
        int characterAuraLvl = character.getAuraLvl();

        Map<String, Integer> characterStats = character.getEquipmentStatsSum();
        int characterHp = characterStats.get("endurance");
        int characterStrength = characterStats.get("strength");
        int characterAgility = characterStats.get("agility");
        int characterLuck = characterStats.get("luck");

        int opponentHp = opponent.getBaseEndurance() * characterAuraLvl;
        int opponentStrength = opponent.getBaseStrength() * characterAuraLvl;
        int opponentAgility = opponent.getBaseAgility() * characterAuraLvl;
        int opponentLuck = opponent.getBaseLuck() * characterAuraLvl;

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
