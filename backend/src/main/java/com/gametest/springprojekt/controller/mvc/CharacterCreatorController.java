package com.gametest.springprojekt.controller.mvc;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/character-creator")
public class CharacterCreatorController {
    public String characterCreator() {
        return "character-creator";
    }
}
