package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.BoxerResultDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.BoxerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boxer")
public class BoxerController {
    private final UserRepository userRepository;
    private final BoxerService boxerService;

    public BoxerController(UserRepository userRepository,  BoxerService boxerService) {
        this.userRepository = userRepository;
        this.boxerService = boxerService;
    }

    @PostMapping("/play")
    public ResponseEntity<BoxerResultDto> playBoxer(@RequestBody int bet) {
        return ResponseEntity.ok(boxerService.playBoxer(getCurrentCharacter(), bet));
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
