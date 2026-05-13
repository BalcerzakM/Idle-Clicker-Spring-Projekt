package com.gametest.springprojekt.controller.mvc;

import ch.qos.logback.core.model.Model;
import com.gametest.springprojekt.service.QuestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/quest")
public class QuestController {
    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

//    @GetMapping
//    public String showQuests(Model model) {
//
//    }
}
