package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.CombatService;
import org.springframework.http.ResponseEntity;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.service.QuestService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/quest")
public class QuestController {
    private final QuestService questService;
    private final CharacterService characterService;
    private final CombatService combatService;


    public QuestController(QuestService questService, CharacterService characterService, CombatService combatService) {
        this.questService = questService;
        this.characterService = characterService;
        this.combatService = combatService;
    }


    @GetMapping()
    public List<QuestDto> showQuests() {

        CharacterEntity character = characterService.getCurrentCharacter();

        List<QuestEntity> questlist = questService.getRandomQuestList();

        return questService.generateQuestDtoList(questlist, character);
    }

    @GetMapping("/active")
    public ResponseEntity<ActiveQuestDto> getActiveQuest() {
        CharacterEntity character = characterService.getCurrentCharacter();

        if (character == null) {
            System.out.println("No character found");
            return ResponseEntity.notFound().build();
        }

        ActiveQuestDto dto = questService.getActiveQuestDto(character);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/active")
    public ResponseEntity<ActiveQuestDto> setActiveQuest(@RequestBody Long id) {
        CharacterEntity character = characterService.getCurrentCharacter();

        ActiveQuestDto quest = questService.setActiveQuest(character, id);

        return ResponseEntity.ok(quest);
    }

    @PostMapping("/combat") //
    public ResponseEntity<CombatDto> getCombatSequence() {
        CharacterEntity character = characterService.getCurrentCharacter();
        CombatDto combatDto = combatService.startCombat(character);
        return ResponseEntity.ok(combatDto);
    }
}
