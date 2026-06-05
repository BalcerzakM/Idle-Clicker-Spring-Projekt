package com.gametest.springprojekt.controller.mvc;


import com.gametest.springprojekt.dto.CharacterCreatorDto;
import com.gametest.springprojekt.model.CharacterClassEntity;
import com.gametest.springprojekt.repository.CharacterClassRepository;
import com.gametest.springprojekt.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mvc/character-creator")
@RequiredArgsConstructor
public class CharacterCreatorController {
    private final CharacterService characterService;
    private final CharacterClassRepository characterClassRepository;

    @GetMapping
    public String characterCreator(Model model) {
        model.addAttribute("characterCreatorDto", new CharacterCreatorDto());

        List<CharacterClassEntity> availableClasses = characterClassRepository.findAll();
        model.addAttribute("availableClasses", availableClasses);

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
