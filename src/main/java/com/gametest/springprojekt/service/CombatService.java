package com.gametest.springprojekt.service;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.OpponentEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CombatService {
    CharacterRepository characterRepository;
    QuestRepository questRepository;

    public CombatService(CharacterRepository characterRepository, QuestRepository questRepository) {
        this.characterRepository = characterRepository;
        this.questRepository = questRepository;
    }



    public List<String> startFistBattle(CharacterEntity character) {
        QuestEntity quest = character.getActiveQuest();

        List<String> combatSequence = new ArrayList<>();
        int charStr = character.getStrength();
        int charHp = character.getEndurance();

        OpponentEntity opponent = quest.getOpponent();
         int oppStr = opponent.getBaseStrength(); //tutaj mnożniki do obrażeń przeciwnika, chyba że zrobić dziedziczenie i wtedy jedena metoda do walk Pvp i pve
         int oppHp = opponent.getBaseEndurance(); // tu do HP

        combatSequence.add("Zaczęto walkę z "+ opponent.getName());
         while (charHp > 0 && oppHp > 0) {
             oppHp -= charStr;
             combatSequence.add(character.getName() + "atakuje " + opponent.getName() + " za " + oppStr + " dmg");
             if (oppHp <= 0) {
                 combatSequence.add("Pokonano " +opponent.getName());
                 character.grantQuestReward(quest);
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
        return combatSequence;
    }



}
