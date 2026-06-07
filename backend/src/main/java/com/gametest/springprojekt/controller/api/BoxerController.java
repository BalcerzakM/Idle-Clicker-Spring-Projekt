package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.BoxerResultDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.BoxerService;
import com.gametest.springprojekt.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boxer")
@RequiredArgsConstructor
public class BoxerController {
    private final CharacterService characterService;
    private final BoxerService boxerService;

    @PostMapping("/play")
    public ResponseEntity<BoxerResultDto> playBoxer(@RequestBody int bet) {
        return ResponseEntity.ok(boxerService.playBoxer(characterService.getCurrentCharacter(), bet));
    }

}
