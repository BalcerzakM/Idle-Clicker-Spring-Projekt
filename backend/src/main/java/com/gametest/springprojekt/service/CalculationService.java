package com.gametest.springprojekt.service;

import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class CalculationService {
    private final SecureRandom random = new SecureRandom();

    /*
        ######################## NAGRODY ZA QUESTY ########################
    */
    public int calculateQuestMoneyReward(CharacterEntity character, QuestEntity quest) {
        int moneyReward = quest.getQuestTier().getMultiplier() * character.getAuraLvl()*2;
        if (quest.getQuestTier() == QuestTier.BOSS) {
            moneyReward *= character.getCurrentBoss();
        }
        return moneyReward;
    }

    public int calculateQuestAuraReward(CharacterEntity character, QuestEntity quest) {
        int auraReward = quest.getQuestTier().getMultiplier() * character.getAuraLvl()*2;
        if (quest.getQuestTier() == QuestTier.BOSS) {
            auraReward *= character.getCurrentBoss();
        }
        return auraReward;
    }

    /*
        ######################## CZAS QUESTÓW ########################
    */
    public long calculateQuestDuration(CharacterEntity character, QuestEntity quest) {
        int auraLvl = character.getAuraLvl();
        double exponent = 1.2;
        int questTierVariable = quest.getQuestTier().getMultiplier();
        long finalQuestTime = (long) (questTierVariable * Math.pow(auraLvl, exponent));

        if (character.getActiveVehicle() != null) {
            int reductionPercent = character.getActiveVehicle().getBaseVehicle().getTimeReductionPercent();
            double multiplier = 1.0 - (reductionPercent / 100.0);
            finalQuestTime = (long) (finalQuestTime * multiplier);
        }
        return finalQuestTime;
    }

    public Instant calculateQuestEndTime(Instant startTime,long questDuration) {
        return startTime.plusSeconds(questDuration);
    }

    /*
        ######################## WALKA ########################
    */
    public int calculateOpponentStat(CharacterEntity character, int stat, QuestTier questTier) {
        if (!(questTier == QuestTier.BOSS)) {
            return (stat + questTier.getMultiplier()) * character.getAuraLvl();
        }
        return stat;
    }

    //stara wersja
//    public int calculateRizzFightBaseDamage(int baseRizz, int baseAgility) {
//        return (int) ((baseRizz + (0.5 * baseAgility)));
//    }

    //wersja z bonusem do dmg dla tego kto mam wieksze agility, wieksza roznica -> wiekszy bonus
    public int calculateRizzFightBaseDamage(int rizz, int agility, int enemyAgility) {
        int agilityDifference = agility - enemyAgility;

        if (agilityDifference <= 0) {
            return Math.max(rizz, 1);
        }

        int agilityBonus = (int) Math.sqrt(agilityDifference) * 3;

        return Math.max(rizz + agilityBonus, 1);
    }

    public boolean didDodge(int agility) {
        final int ROLL_MAX = 10_000;

        final double MAX_DODGE_CHANCE = 0.75; //max 75% szansy na unik
        final double AGILITY_SCALE = 1000.0;

        agility = Math.max(agility, 0);

        double dodgeChance = MAX_DODGE_CHANCE * (agility / (agility + AGILITY_SCALE));

        int dodgeChanceRoll = (int) (dodgeChance * ROLL_MAX);

        return random.nextInt(ROLL_MAX) < dodgeChanceRoll;
    }

    //moze jakis pierwiastek na tego kryta zeby mogl byc 100% ale wolno lecial
    public int calculateDamage(int baseDamage, int luck) {
        if (baseDamage <= 0) baseDamage = 1;

        int minDmg = (int) (baseDamage * 0.8);
        int maxDmg = (int) (baseDamage * 1.2);
        int actualDamage = random.nextInt(maxDmg - minDmg + 1) + minDmg;

        if(random.nextInt(10000) + luck > 9000) {
            actualDamage *= 2;
        }
        return Math.max(actualDamage, 1);
    }

    /*
        ######################## ITEMY ########################
    */
    //narazie oddzielnie bo nie wiem jeszcze czy roznych algorytmow nie porobic dla kazdej statystyki
//    public int calculateItemBonusStat(int stat, CharacterEntity character, int amplifier) {
//        return stat * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
//    }

    public int calculateItemBonusRizz(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        return baseItem.getBaseRizz() * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
    }

    public int calculateItemBonusStrength(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        return baseItem.getBaseStrength() * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
    }

    public int calculateItemBonusAgility(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        return baseItem.getBaseAgility() * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
    }

    public int calculateItemBonusEndurance(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        return baseItem.getBaseEndurance() * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
    }

    public int calculateItemBonusLuck(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        return baseItem.getBaseLuck() * (random.nextInt(character.getAuraLvl() + amplifier) + 1);
    }


    public int calculateItemPrice(BaseItemEntity baseItem, CharacterEntity character, int statsSum) {
        return baseItem.getBasePrice() * (random.nextInt(character.getAuraLvl()) + 1) + statsSum;
    }
}
