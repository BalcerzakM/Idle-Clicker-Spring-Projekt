package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drinks")
@RequiredArgsConstructor
public class DrinkShopController {
    private final CharacterService characterService;
    private final DrinkService drinkService;

    @GetMapping
    public List<ItemDto> getDrinks() {
        CharacterEntity character = characterService.getCurrentCharacter();
        return drinkService.getDrinkOffers(character);
    }

    @PostMapping("/buy")
    public ResponseEntity<ItemDto> buyDrink(@RequestBody Long drinkId) {
        CharacterEntity character = characterService.getCurrentCharacter();
        return ResponseEntity.ok(drinkService.buyDrink(character, drinkId));
    }
}
