package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.dto.CombatDto;
import com.gametest.springprojekt.exception.NoActiveQuest;
import com.gametest.springprojekt.service.CombatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.QuestService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/quest")
public class QuestController {
    private final QuestService questService;
    private final UserRepository userRepository;
    private final CombatService combatService;


    public QuestController(QuestService questService,  UserRepository userRepository, CombatService combatService) {
        this.questService = questService;
        this.userRepository = userRepository;
        this.combatService = combatService;
    }


    @GetMapping()
    public List<QuestDto> showQuests() {

        CharacterEntity character = this.getCurrentCharacter();

        List<QuestEntity> questlist = questService.getRandomQuestList();

        return questService.generateQuestDtoList(questlist, character);
    }

    @GetMapping("/active")
    public ResponseEntity<ActiveQuestDto> getActiveQuest() {
        CharacterEntity character = this.getCurrentCharacter();
        if (character == null) {
            System.out.println("No character found");
            return ResponseEntity.notFound().build();
        }
        try {
            ActiveQuestDto dto = questService.getActiveQuestDto(character);
            return ResponseEntity.ok(dto);
        } catch (NoActiveQuest e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/active")
    public ResponseEntity<ActiveQuestDto> setActiveQuest(@RequestBody Long id) {
        CharacterEntity character = this.getCurrentCharacter();

        ActiveQuestDto quest = questService.setActiveQuest(character, id);

        return ResponseEntity.ok(quest);
    }


    private CharacterEntity getCurrentCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika!"));

        // na razie na sztywno, z listy pierwsza postać po prostu
        return user.getCharacters().getFirst();
    }

    @PostMapping("/combat") //
    public ResponseEntity<CombatDto> getCombatSequence() {
        CharacterEntity character = this.getCurrentCharacter();
        CombatDto combatDto =combatService.startFistBattle(character);
        return ResponseEntity.ok(combatDto);
    }
}
