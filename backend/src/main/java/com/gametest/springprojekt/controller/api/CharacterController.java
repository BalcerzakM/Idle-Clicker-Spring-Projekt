package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.FullCharacterInfoDto;
import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.dto.ShortCharacterInfoDto;
import com.gametest.springprojekt.dto.SwapRequestDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.enums.StatName;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/character")
public class CharacterController {
    CharacterRepository characterRepository;
    CharacterService characterService;


    public CharacterController(CharacterRepository characterRepository, CharacterService characterService) {
        this.characterRepository = characterRepository;
        this.characterService = characterService;
    }

    //pieniadze, krysztaly, awatar, aura i aura level
    @GetMapping("/shortInfo")
    public ResponseEntity<ShortCharacterInfoDto> getShortCharacterInfo() {
        CharacterEntity character = characterService.getCurrentCharacter();
        ShortCharacterInfoDto shortCharacterInfoDto = characterService.getShortCharacterInfo(character);
        return ResponseEntity.ok(shortCharacterInfoDto);
    }

    @GetMapping("/fullInfo")
    public ResponseEntity<FullCharacterInfoDto> getFullCharacterInfo() {
        CharacterEntity character = characterService.getCurrentCharacter();
        FullCharacterInfoDto fullCharacterInfoDto = characterService.getFullCharacterInfo(character);
        return ResponseEntity.ok(fullCharacterInfoDto);
    }

    /**
     * metoda do pobrania informacji do widoku profil i sklep (przedmioty plus statystki)
     *
     * @return
     */
    @GetMapping("/statsItems")
    public ResponseEntity<ItemsAndStatsDto> getCharacterInfo() {
        CharacterEntity character = characterService.getCurrentCharacter();
        ItemsAndStatsDto result = characterService.getItemsAndStats(character);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/equip")// nie do końca wiem czy warto tu coś zwracać
    public ResponseEntity<?> equipItem(
            @Valid @RequestBody SwapRequestDto swapRequest
    ) {
        CharacterEntity character = characterService.getCurrentCharacter();
        characterService.equip(character,
                swapRequest.getEquipmentItemId(),
                swapRequest.getBackpackItemId()
        );
        return ResponseEntity.ok("Wyposażono przedmiot");
    }

    @PatchMapping("/statIncrement")
    public ResponseEntity<?> incrementStat(
            @RequestParam StatName stat) {
        CharacterEntity character = characterService.getCurrentCharacter();
        characterService.incrementStat(character, stat, 1);
        return ResponseEntity.ok("Zwiększono statystykę");
    }


}
