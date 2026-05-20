package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.exception.NoActiveQuest;
import com.gametest.springprojekt.exception.QuestStillActiveException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.OpponentEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CombatService {
    CharacterRepository characterRepository;
    QuestRepository questRepository;
    QuestService questService;

    public CombatService(CharacterRepository characterRepository, QuestRepository questRepository, QuestService questService) {
        this.characterRepository = characterRepository;
        this.questRepository = questRepository;
        this.questService = questService;
    }


    @Transactional
    public CombatDto startFistBattle(CharacterEntity character) {

        ActiveQuestDto activeQuest = questService.getActiveQuestDto(character); // w tym jest sprawdzenie czy użytkownik ma aktywnegoQuesta
        if (Instant.now().isBefore(activeQuest.getQuestEndTime())) { // tu sprawdzamy czy użytkownik go już skończył
            throw new QuestStillActiveException();
        }

        QuestEntity quest = character.getActiveQuest();

        List<String> combatSequence = new ArrayList<>();
        int charStr = character.getStrength();
        int charHp = character.getEndurance();

        OpponentEntity opponent = quest.getOpponent();
         int oppStr = opponent.getBaseStrength(); //tutaj mnożniki do obrażeń przeciwnika, chyba że zrobić dziedziczenie i wtedy jedena metoda do walk Pvp i pve
         int oppHp = opponent.getBaseEndurance(); // tu do HP

        int bonusAura = quest.calculateAuraReward(character); //liczenie nagród lvla
        int bonusMoney = quest.calculateMoneyReward(character); // liczenie nagród siana nie wiem czy nie lepiej tego przekazywać jakoś pbo to często liczone będzie

        combatSequence.add("Zaczęto walkę z "+ opponent.getName());
         while (charHp > 0 && oppHp > 0) {
             oppHp -= charStr;
             combatSequence.add(character.getName() + "atakuje " + opponent.getName() + " za " + charStr + " dmg");
             if (oppHp <= 0) {
                 combatSequence.add("Pokonano " +opponent.getName());
                 character.grantQuestReward(bonusAura, bonusMoney);
                 break;
             }
             charHp -= oppStr;
             combatSequence.add(opponent.getName()+ "atakuje " + character.getName() + " za " + oppStr + " dmg");
             if (charHp <= 0) {
                 combatSequence.add("Pokonał cię " +opponent.getName());
                 character.setActiveQuest(null);
                 break;
             }
         }
         characterRepository.save(character);
         return new CombatDto(combatSequence,bonusMoney,bonusAura);
    }



}
