package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.BouncerDutyDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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
        Instant dutyEndTime = character.getBouncerDutyEndTime();
//        if (dutyEndTime == null) {
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(new BouncerDutyDto(dutyEndTime));
    }

    @PostMapping
    public ResponseEntity<BouncerDutyDto> startBouncerDuty(
            @RequestBody Integer hours
            ){
        CharacterEntity character = characterService.getCurrentCharacter();
        return ResponseEntity.ok(characterService.startBouncerDuty(character, hours));
    }
}
