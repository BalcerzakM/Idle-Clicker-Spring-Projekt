package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CharacterInBattleDto;
import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.GangCombatDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.*;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import com.gametest.springprojekt.model.mapper.ItemMapper;
import com.gametest.springprojekt.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CombatService {
    private final ItemTokenService itemTokenService;
    private final QuestRepository questRepository;
    private final CalculationService calculationService;
    private final ItemMapper itemMapper;


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
        String enemyImageFolder = "opponents";
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
                rewardItemDto = itemMapper.toDto(rewardItem);
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
                enemyImageFolder,
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
        String enemyImageFolder = "bosses";
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
                rewardItemDto = itemMapper.toDto(rewardItem);
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
                enemyImageFolder,
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

    private static class FighterState {
        int currentHp;
        final int strength, agility, luck, rizz, baseRizzDmg;

        FighterState(CharacterEntity ch, CalculationService calc) {
            Map<String, Integer> stats = ch.getEquipmentStatsSum();
            this.currentHp = stats.get("endurance");
            this.strength = stats.get("strength");
            this.agility = stats.get("agility");
            this.luck = stats.get("luck");
            this.rizz = stats.get("rizz");
            this.baseRizzDmg = calc.calculateRizzFightBaseDamageOld(rizz, agility);//janku abyś gnił za te twoje widzimisie
        }
    }


    private List<Integer> simulateMixedCombat(FighterState character, FighterState opponent) {

        List<Integer> combatLog = new ArrayList<>();
        boolean playersAttack = true;
        int attacktype =0;
        int dmg;
        while (character.currentHp > 0 && opponent.currentHp > 0) {
            if(playersAttack) {
                if(calculationService.didDodge(opponent.agility)) {
                    combatLog.add(0);
                } else {
                    if(attacktype % 2 == 0) {
                        dmg = calculationService.calculateDamage(character.strength, character.luck);
                    }
                    else{
                        dmg = calculationService.calculateDamage(character.baseRizzDmg, character.luck);
                    }
                    combatLog.add(dmg);
                    opponent.currentHp -= dmg;
                }
                playersAttack = false;
            } else {
                if(calculationService.didDodge(character.agility)) {
                    combatLog.add(0);
                } else {
                    if(attacktype % 2 == 0) {
                        dmg = calculationService.calculateDamage(opponent.strength, opponent.luck);
                    }
                    else{
                        dmg = calculationService.calculateDamage(opponent.baseRizzDmg, opponent.luck);
                    }
                    combatLog.add(dmg);
                    character.currentHp -= dmg;
                }
                attacktype++;
                playersAttack = true;
            }
        }
        return combatLog;
    }


    @Transactional
    public CombatDto startPlayerCombat(CharacterEntity character, CharacterEntity opponent) {
        if (character.getBackpack().size() >= character.getMAX_BACKPACK_SLOTS()) {
            throw new BackpackIsAlreadyFullException("Twój plecak jest pełny! Zrób w nim miejsce, zanim ruszysz do walki.");
        }

        if (character == opponent) {
            throw new CannotAttackSelfException("Nie można zaatakować samego siebie.");
        }

        FighterState player1 = new FighterState(character, calculationService);
        FighterState player2 = new FighterState(opponent, calculationService);

        int characterHp = player1.currentHp;

        int opponentHp = player2.currentHp;

        List<Integer> combatLog;

        combatLog = simulateMixedCombat(player1, player2);


        String enemyName = opponent.getName();
        String enemyImageFolder = "avatars";
        String enemyImagePath = opponent.getAvatarPicture();
        boolean playerWon = combatLog.size() % 2 != 0;

        int bonusAura = 0;
        int bonusMoney = 0;
        ItemDto rewardItemDto = null;

        if(playerWon) {
            bonusAura = opponent.getAuraLvl()*10; //aktualnie na sztywno jako nagroda dziesięciokrotność poziomu pokonanej postaci
            bonusMoney = opponent.getAuraLvl()*10;

            ItemEntity rewardItem = itemTokenService.handleRewardToken(false);

            if (rewardItem != null) {
                rewardItemDto = itemMapper.toDto(rewardItem);
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
                enemyImageFolder,
                enemyImagePath,
                "MIXED_FIGHT",
                bonusMoney,
                bonusAura,
                rewardItemDto
        );
    }


    @Transactional
    public GangCombatDto startGangCombat(List<CharacterEntity> gangA, List<CharacterEntity> gangB){
        //czy jest miejsce w plecaku
        for (CharacterEntity ch : gangA) {
            if (ch.getBackpack().size() >= ch.getMAX_BACKPACK_SLOTS()) {
                throw new BackpackIsAlreadyFullException("Plecak postaci " + ch.getName() + " jest pełny!");
            }
        }

        // Sprawdzenie, czy nie ma wspólnych postaci
        Set<Long> ids = gangA.stream().map(CharacterEntity::getId).collect(Collectors.toSet());
        if (gangB.stream().anyMatch(e -> ids.contains(e.getId()))) {
            throw new CannotAttackSelfException("Nie można walczyć z własną drużyną.");
        }

        List<CharacterInBattleDto> teamACharacters = new ArrayList<>();

        List<CharacterInBattleDto> teamBCharacters = new ArrayList<>();

        List<Integer> combatLog = new ArrayList<>();

        //Indeksy aktualnie walczących
        int indexA = 0;
        int indexB = 0;

        //Stany bojowe inicjowane przy starcie
        FighterState fighterA = null;
        FighterState fighterB = null;

        while (indexA < gangA.size() && indexB < gangB.size()) {

            //Jeśli nie ma jeszcze aktywnego zawodnika (początek lub poprzedni przegrał)
            if (fighterA == null || fighterA.currentHp <= 0) {
                CharacterEntity nextA = gangA.get(indexA);
                fighterA = new FighterState(nextA, calculationService);

                teamACharacters.add(new CharacterInBattleDto(nextA.getName(), nextA.getAvatarPicture(), fighterA.currentHp));//dodanie do dto dla frontendu
            }

            if (fighterB == null || fighterB.currentHp <= 0) {
                CharacterEntity nextB = gangB.get(indexB);
                fighterB = new FighterState(nextB, calculationService);
                teamBCharacters.add(new CharacterInBattleDto(nextB.getName(), nextB.getAvatarPicture(), fighterB.currentHp));
            }

            //walka danej pary
            List<Integer> roundLog = simulateMixedCombat(fighterA, fighterB);
            combatLog.addAll(roundLog);

            //Sprawdzamy, kto przegrał i przesuwamy indeks jego drużyny
            if (fighterA.currentHp <= 0) {
                indexA++;//nastepny z a
                fighterA = null; // wymuszamy stworzzenie nowego fighterState
            } else {
                indexB++;
                fighterB = null;
            }
        }

        String teamAImageFolder= "avatars";
        String teamBImageFolder = "avatars"; // na razie na sztywno, ale to trzeba zmienić przy walkach PvE

        boolean gangAWon = indexA < gangA.size();//jeśli gang A nie wyczerpał listy to znaczy ze wygrał

        int bonusAura = 0;
        int bonusMoney = 0;
        ItemDto rewardItemDto = null;

        if(gangAWon) {
            bonusAura = 100; //trzeba pomyśleć o nagrodach imo kryształ i numerek oprócz siana i aury
            bonusMoney = 100;

            ItemEntity rewardItem = itemTokenService.handleRewardToken(false);

            if (rewardItem != null) {
                rewardItemDto = itemMapper.toDto(rewardItem);
            }

            for (CharacterEntity ch : gangA) {
                ch.grantQuestReward(bonusAura, bonusMoney, rewardItem);
            }
        }


        return new GangCombatDto(
                combatLog,
                gangAWon,
                teamAImageFolder,
                teamBImageFolder,
                teamACharacters,
                teamBCharacters,
                bonusMoney,
                bonusAura,
                rewardItemDto
        );
    }

}
