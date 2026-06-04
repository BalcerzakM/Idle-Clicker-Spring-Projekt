package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.ItemShopService;
import com.gametest.springprojekt.service.ItemTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ItemShopController {
    private final ItemShopService itemShopService;
    private final ItemTokenService itemTokenService;
    private final CharacterService characterService;

    public ItemShopController(ItemShopService itemShopService, ItemTokenService itemTokenService, CharacterService characterService) {
        this.itemShopService = itemShopService;
        this.itemTokenService = itemTokenService;
        this.characterService = characterService;
    }

    @GetMapping
    public List<ItemDto> showItems() {
        return itemShopService.getTodayShopItemDto(characterService.getCurrentCharacter());
    }

    //do debugu
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshCharacterOffers() {
        itemShopService.refreshShopOfferForCrystals(characterService.getCurrentCharacter());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/buy")
    public ResponseEntity<ItemDto> buyItemFromOffer(@RequestBody Long shopOfferId) {
        ItemDto itemDto = itemShopService.buyItemFromOffer(characterService.getCurrentCharacter(), shopOfferId);

        return ResponseEntity.ok(itemDto);
    }

    @PostMapping("/sell")
    public void sellItemToShop(@RequestBody Long backpackItemId) {
        itemShopService.sellItem(characterService.getCurrentCharacter(), backpackItemId);
    }

    @PostMapping("/spendToken")
    public ResponseEntity<ItemDto> spendToken(@RequestBody Long backpackTokenId) {
        ItemDto itemDto = itemTokenService.spendItemToken(characterService.getCurrentCharacter(), backpackTokenId);

        return ResponseEntity.ok(itemDto);
    }

}
