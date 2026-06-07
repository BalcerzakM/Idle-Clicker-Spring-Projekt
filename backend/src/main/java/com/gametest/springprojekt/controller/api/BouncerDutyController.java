package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.BouncerDutyDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class BouncerDutyController {


    private final CharacterService characterService;

    public BouncerDutyController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public ResponseEntity<BouncerDutyDto> getBouncerDuty() {
        CharacterEntity character = characterService.getCurrentCharacter();
        BouncerDutyDto dto = characterService.getBouncerDutyDto(character);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BouncerDutyDto> startBouncerDuty(
            @RequestBody Integer hours
            ){
        CharacterEntity character = characterService.getCurrentCharacter();
        return ResponseEntity.ok(characterService.startBouncerDuty(character, hours));
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeBouncerDuty() {
        CharacterEntity character = characterService.getCurrentCharacter();
        try {
            int reward = characterService.completeBouncerDuty(character);
            return ResponseEntity.ok(Map.of("reward", reward));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
