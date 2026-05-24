package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ShopItemDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.BoxerService;
import com.gametest.springprojekt.service.ItemShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ItemShopController {
    private final UserRepository userRepository;
    private final ItemShopService itemShopService;

    public ItemShopController(UserRepository userRepository, ItemShopService itemShopService) {
        this.userRepository = userRepository;
        this.itemShopService = itemShopService;
    }

    @GetMapping
    public List<ShopItemDto> showItems() {
        return itemShopService.getTodayShopItemDto(getCurrentCharacter());
    }

    //do debugu
    @PostMapping("/refresh")
    public void refreshOffers() {
        itemShopService.refreshShopOffers();
    }

    //WIP do testu
    @PostMapping("buy")
    public ResponseEntity<ShopItemDto> buyItemFromOffer(@RequestBody Long shopOfferId) {
            ShopItemDto shopItemDto = itemShopService.buyItemFromOffer(getCurrentCharacter(), shopOfferId);

            return ResponseEntity.ok(shopItemDto);
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
