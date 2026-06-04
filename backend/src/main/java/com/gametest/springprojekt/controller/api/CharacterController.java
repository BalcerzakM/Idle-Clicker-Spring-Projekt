package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.dto.MoneyAndAvatarDto;
import com.gametest.springprojekt.dto.SwapRequestDto;
import com.gametest.springprojekt.exception.BackpackItemNotFoundException;
import com.gametest.springprojekt.exception.EquipmentItemNotFoundException;
import com.gametest.springprojekt.exception.InvalidSlotException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    @GetMapping("/money")
    public ResponseEntity<MoneyAndAvatarDto> getMoney() {
        CharacterEntity character = characterService.getCurrentCharacter();
        MoneyAndAvatarDto moneyAndAvatarDto = characterService.getMoneyAndAvatar(character);
        return ResponseEntity.ok(moneyAndAvatarDto);
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




}
