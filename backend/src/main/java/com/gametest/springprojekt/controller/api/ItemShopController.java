package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
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

    //traj kacze porobic trzeba

    @GetMapping
    public List<ItemDto> showItems() {
        return itemShopService.getTodayShopItemDto(getCurrentCharacter());
    }

    //do debugu
    @PostMapping("/refresh")
    public void refreshOffers() {
        itemShopService.refreshShopOffers();
    }


    @PostMapping("buy")
    public ResponseEntity<ItemDto> buyItemFromOffer(@RequestBody Long shopOfferId) {
            ItemDto itemDto = itemShopService.buyItemFromOffer(getCurrentCharacter(), shopOfferId);

            return ResponseEntity.ok(itemDto);
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
