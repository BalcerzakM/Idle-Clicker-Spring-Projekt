package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.dto.SpecialQuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.CombatService;
import com.gametest.springprojekt.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boss")
@RequiredArgsConstructor
public class SpecialQuestController {
    QuestService questService;
    CharacterService characterService;
    CombatService combatService;

    @GetMapping
    public ResponseEntity<SpecialQuestDto> getSpecialQuest() {
        CharacterEntity character = characterService.getCurrentCharacter();

        SpecialQuestDto specialQuestDto = questService.getCurrentBoss(character);
        return ResponseEntity.ok(specialQuestDto);
    }

    @PostMapping("/combat")
    public ResponseEntity<CombatDto> getBossCombatSequence() {
        CharacterEntity character = characterService.getCurrentCharacter();

        CombatDto combatDto = combatService.startBossCombat(character);
        return ResponseEntity.ok(combatDto);
    }
}
