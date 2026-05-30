package com.gametest.springprojekt.controller.mvc;


import com.gametest.springprojekt.dto.CharacterCreatorDto;
import com.gametest.springprojekt.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/character-creator")
public class CharacterCreatorController {
    private final CharacterService characterService;

    public CharacterCreatorController(CharacterService characterService) {
        this.characterService = characterService;
    }


    @GetMapping
    public String characterCreator(Model model) {
        model.addAttribute("characterCreatorDto", new CharacterCreatorDto());
        return "character-creator";
    }

    @PostMapping
    public String createCharacter(
        @Valid @ModelAttribute("characterCreatorDto") CharacterCreatorDto dto,
        BindingResult bindingResult,
        Authentication authentication,
        Model model) {
        if (bindingResult.hasErrors()) {
            return "character-creator";
        }

        try {
            String username = authentication.getName();
            characterService.createAndAssignCharacter(dto, username);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "character-creator";
        }
    }
}
