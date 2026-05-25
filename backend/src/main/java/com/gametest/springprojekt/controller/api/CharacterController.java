package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.dto.MoneyDto;
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

    /**
     * metoda do pobrania informacji do widoku profil i sklep (przedmioty plus statystki)
     *
     * @return
     */
    @GetMapping("/statsItems")
    public ResponseEntity<ItemsAndStatsDto> getCharacterInfo() {
        CharacterEntity character = getCurrentCharacter();
        ItemsAndStatsDto result = characterService.getItemsAndStats(character);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/equip")// nie do końca wiem czy warto tu coś zwracać
    public ResponseEntity<?> equipItem(
            @Valid @RequestBody SwapRequestDto swapRequest
    ) {
        CharacterEntity character = getCurrentCharacter();
        try {
            characterService.equip(character,
                    swapRequest.getEquipmentItemId(),
                    swapRequest.getBackpackItemId()
            );
        } catch (EquipmentItemNotFoundException | BackpackItemNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidSlotException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        return ResponseEntity.ok("Wyposażono przedmiot");
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
