package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ActiveQuestDto;
import com.gametest.springprojekt.exception.NoActiveQuest;
import com.gametest.springprojekt.model.enums.CharacterClass;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.QuestService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/quest")
public class QuestController {
    private final QuestService questService;
    private final UserRepository userRepository;


    public QuestController(QuestService questService,  UserRepository userRepository) {
        this.questService = questService;
        this.userRepository = userRepository;
    }


    @GetMapping()
    public List<QuestDto> showQuests() {

        //na sztywno narazie pierwsza postac z listy
        CharacterEntity character = this.getCurrentCharacter();

        List<QuestEntity> questlist = questService.getRandomQuestList();

        List<QuestDto> quests = questService.generateQuestDtoList(questlist, character);

        return quests;
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


    private CharacterEntity getCurrentCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.getByUsername(username);

        // na razie na sztywno z listy pierwszy po prostu
        return user.getCharacters().get(0);
    }
}
