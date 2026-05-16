package com.gametest.springprojekt.controller.mvc;

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
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mvc/quest")
public class QuestController {
    private final QuestService questService;
    private final UserRepository userRepository;

    public QuestController(QuestService questService,  UserRepository userRepository) {
        this.questService = questService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showQuests(Model model, Authentication authentication) {
        String username = authentication.getName();

        UserEntity user = userRepository.getByUsername(username);

        //na sztywno narazie pierwsza postac z listy
        CharacterEntity character = user.getCharacters().get(0);

        List<QuestEntity> questlist = questService.getRandomQuestList();

        List<QuestDto> quests = questService.generateQuestDtoList(questlist, character);

        model.addAttribute("quests", quests);

        return "quests";
    }
}
