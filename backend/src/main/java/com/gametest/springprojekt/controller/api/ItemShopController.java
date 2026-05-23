package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.ShopItemDto;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.UserRepository;
import com.gametest.springprojekt.service.BoxerService;
import com.gametest.springprojekt.service.ItemShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return itemShopService.getTodayShopItemDto();
    }

    //do debugu
    @PostMapping("/refresh")
    public void refreshOffer() {
        itemShopService.refreshShopOffers();
    }


}
