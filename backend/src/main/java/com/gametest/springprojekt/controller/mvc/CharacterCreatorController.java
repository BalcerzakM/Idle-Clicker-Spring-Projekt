package com.gametest.springprojekt.controller.mvc;


import com.gametest.springprojekt.dto.CharacterCreatorDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/character-creator")
public class CharacterCreatorController {

    @GetMapping
    public String characterCreator(
            Model model
    ) {
        model.addAttribute("characterCreatorDto", new CharacterCreatorDto());
        return "character-creator";
    }
}
