package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.CombatService;
import com.gametest.springprojekt.service.QuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boss")
public class SpecialQuestController {
    QuestService questService;
    CharacterService characterService;
    CombatService combatService;

    public SpecialQuestController(QuestService questService, CharacterService characterService,  CombatService combatService) {
        this.questService = questService;
        this.characterService = characterService;
        this.combatService = combatService;
    }


    @GetMapping
    public ResponseEntity<QuestDto> getSpecialQuest() {
        CharacterEntity character = characterService.getCurrentCharacter();

        QuestDto questDto = questService.getCurrentBoss(character);
        return ResponseEntity.ok(questDto);
    }

    @PostMapping("/combat")
    public ResponseEntity<CombatDto> getBossCombatSequence() {
        CharacterEntity character = characterService.getCurrentCharacter();

        CombatDto combatDto = combatService.startBossCombat(character);
        return ResponseEntity.ok(combatDto);
    }
}
