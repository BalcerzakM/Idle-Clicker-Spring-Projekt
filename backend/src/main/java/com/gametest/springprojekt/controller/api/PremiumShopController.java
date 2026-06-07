package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.PremiumOfferDto;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.PremiumShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/premium")
public class PremiumShopController {
    private final PremiumShopService premiumShopService;
    private final CharacterService characterService;

    public PremiumShopController(PremiumShopService premiumShopService, CharacterService characterService) {
        this.premiumShopService = premiumShopService;
        this.characterService = characterService;
    }

    @GetMapping
    public List<PremiumOfferDto> getPremiumOffers() {
        return premiumShopService.getPremiumOffers();
    }

    @PostMapping("/buyCristals")
    public ResponseEntity<?> buyCristals(@RequestBody int offerId) {

        return ResponseEntity.ok().build();
    }
}
