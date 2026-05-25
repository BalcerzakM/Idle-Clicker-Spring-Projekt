package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.MoneyDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/character")
public class CharacterController {
    CharacterRepository characterRepository;
    UserRepository userRepository;
    CharacterService characterService;


    public CharacterController(CharacterRepository characterRepository, UserRepository userRepository, CharacterService characterService) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
        this.characterService = characterService;
    }


    @GetMapping("/money")
    public ResponseEntity<MoneyDto> getMoney() {
        CharacterEntity character = getCurrentCharacter();
        MoneyDto moneyDto = characterService.getMoney(character);
        return ResponseEntity.ok(moneyDto);
    }


    private CharacterEntity getCurrentCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika!"));

        // na razie na sztywno, z listy pierwsza postać po prostu
        return user.getCharacters().getFirst();
    }
}
