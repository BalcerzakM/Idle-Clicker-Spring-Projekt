package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ShopItemDto;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.ShopOfferEntity;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.ShopOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ItemShopService {
    private final ItemRepository itemRepository;
    private final SecureRandom random = new SecureRandom();
    private final ShopOfferRepository shopOfferRepository;
    private final int NUMBER_OF_SHOP_ITEMS = 3; //narazie 3 zeby w dataloaderze nie dodawac za duzo

    public ItemShopService(ItemRepository itemRepository, ShopOfferRepository shopOfferRepository) {
        this.itemRepository = itemRepository;
        this.shopOfferRepository = shopOfferRepository;
    }

    //sciaga dzisiejsza oferte z bazy danych
    public List<ShopItemDto> getTodayShopItemDto() {
        LocalDate today = LocalDate.now();

        List<ShopOfferEntity> offers = shopOfferRepository.findByOfferDate(today);

//        if (offers.isEmpty()) {
//            offers = generateShopOffer(today);
//        }

        List<ShopItemDto> shopItemDtos = new ArrayList<>();

        for (ShopOfferEntity offerEntity : offers) {
            shopItemDtos.add(generateShopItemDto(offerEntity));
        }

        return shopItemDtos;
    }

    //tworzy nowa oferte i zapisuje ja do bazy danych
    private List<ShopOfferEntity> generateShopOffer(LocalDate date) {
        List<ShopOfferEntity> shopOffer = new ArrayList<>();
        for (ItemEntity itemEntity : getRandomItems()) {
            shopOffer.add(new ShopOfferEntity(null, date, itemEntity));
        }

        return shopOfferRepository.saveAll(shopOffer);
    }

    //sciaga losowe itemy do oferty z bazy danych
    private Set<ItemEntity> getRandomItems() {
        List<ItemEntity> items = itemRepository.findAll();

        Set<ItemEntity> randomItems = new HashSet<>();

        if (items.size() < NUMBER_OF_SHOP_ITEMS) {
            throw new RuntimeException("Niewystarczajaca liczba przedmiotow w bazie!");
        }

        while (randomItems.size() < NUMBER_OF_SHOP_ITEMS) {
            randomItems.add(items.get(random.nextInt(items.size())));
        }

        return randomItems;
    }

    //generuje dto na podstawie oferty sciagnietej z bazy
    private ShopItemDto generateShopItemDto(ShopOfferEntity shopOfferEntity) {
        ItemEntity item = shopOfferEntity.getItem();
        return new ShopItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getSlotType(),
                item.getBonusRizz(),
                item.getBonusStrength(),
                item.getBonusAgility(),
                item.getBonusEndurance(),
                item.getBonusLuck(),
                item.getPrice(),
                item.getImagePath()
        );
    }

    //o podanej godzinie (6 rano) bedzie odswiezalo oferte
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void refreshShopOffer() {
        generateShopOffer(LocalDate.now());
    }
}
