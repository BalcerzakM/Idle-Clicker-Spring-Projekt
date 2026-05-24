package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ShopItemDto;
import com.gametest.springprojekt.exception.ItemNotFoundException;
import com.gametest.springprojekt.exception.NotEnoughAvailableBaseItemsException;
import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.ShopOfferEntity;
import com.gametest.springprojekt.repository.BaseItemRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.ShopOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemShopService {
    private final ItemRepository itemRepository;
    private final SecureRandom random = new SecureRandom();
    private final ShopOfferRepository shopOfferRepository;
    private final BaseItemRepository baseItemRepository;
    private final CharacterRepository characterRepository;
    private final int NUMBER_OF_SHOP_ITEMS = 3; //narazie 3 zeby w dataloaderze nie dodawac za duzo

    public ItemShopService(ItemRepository itemRepository, ShopOfferRepository shopOfferRepository, BaseItemRepository baseItemRepository, CharacterRepository characterRepository) {
        this.itemRepository = itemRepository;
        this.shopOfferRepository = shopOfferRepository;
        this.baseItemRepository = baseItemRepository;
        this.characterRepository = characterRepository;
    }

    //sciaga dzisiejsza oferte z bazy danych
    public List<ShopItemDto> getTodayShopItemDto(CharacterEntity character) {
        LocalDate today = LocalDate.now();

        List<ShopOfferEntity> offers = shopOfferRepository.findByCharacterAndOfferDate(character, today);

        List<ShopItemDto> shopItemDtos = new ArrayList<>();

        for (ShopOfferEntity offerEntity : offers) {
            shopItemDtos.add(generateShopItemDto(offerEntity));
        }

        return shopItemDtos;
    }

    //WIP narazie do testow
    public ShopItemDto buyItemFromOffer(CharacterEntity character, Long shopOfferId) {
        ShopOfferEntity shopOffer = shopOfferRepository.findById(shopOfferId)
                .orElseThrow(() -> new ItemNotFoundException("Nie znaleziono przedmiotu o podanym ID"));

        shopOfferRepository.delete(shopOffer);
        shopOfferRepository.save(generateRandomShopOffer(character));

        return generateShopItemDto(shopOffer);
    }

    //generuje dto na podstawie oferty sciagnietej z bazy
    private ShopItemDto generateShopItemDto(ShopOfferEntity shopOfferEntity) {
        ItemEntity item = shopOfferEntity.getItem();
        return new ShopItemDto(
                shopOfferEntity.getId(),
                item.getBaseItem().getName(),
                item.getBaseItem().getDescription(),
                item.getBaseItem().getSlotType(),
                item.getBonusRizz(),
                item.getBonusStrength(),
                item.getBonusAgility(),
                item.getBonusEndurance(),
                item.getBonusLuck(),
                item.getPrice(),
                item.getBaseItem().getImagePath()
        );
    }

    //o podanej godzinie (6 rano) odswieza oferte dla kazdego charactera w bazie
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void refreshShopOffers() {
        List<CharacterEntity> characters = characterRepository.findAll();

        for (CharacterEntity character : characters) {
            //nad tym trzeba bedzie sie zastanowic
            shopOfferRepository.deleteByCharacter(character);

            shopOfferRepository.saveAll(generateRandomShopOffers(character));
        }
    }

    //generuje jeden item do oferty bez powtarzajacych sie baseItem, uzywana po np. kupieniu itema do uzupelnienia oferty
    private ShopOfferEntity generateRandomShopOffer(CharacterEntity character) {
        List<ShopOfferEntity> presentOffers = shopOfferRepository.findByCharacterAndOfferDate(character, LocalDate.now());

        Set<Long> usedBaseItemIds = presentOffers.stream().map(offer -> offer
                .getItem()
                .getBaseItem()
                .getId()).collect(Collectors.toSet());

        List<BaseItemEntity> baseItems = baseItemRepository.findAll();

        List<BaseItemEntity> availableBaseItems = baseItems.stream()
                .filter(item -> !usedBaseItemIds
                        .contains(item.getId())).toList();

        if (availableBaseItems.isEmpty()) {
            throw new NotEnoughAvailableBaseItemsException("Brak dostepnych base itemow do wygenerowania oferty!");
        }

        BaseItemEntity basicItem = availableBaseItems.get(random.nextInt(availableBaseItems.size()));

        ItemEntity item = generateItemEntity(basicItem, character);

        return new ShopOfferEntity(null, LocalDate.now(), item, character);
    }

    //tworzy full zestaw oferty, oddzielna metoda by zapobiec duplikatom, ta jest uzywana do pelnego refresha ofert
    private List<ShopOfferEntity> generateRandomShopOffers(CharacterEntity character) {
        List<BaseItemEntity> baseItems = baseItemRepository.findAll();

        if (baseItems.size() < NUMBER_OF_SHOP_ITEMS) {
            throw new NotEnoughAvailableBaseItemsException("Za malo dostepnych base itemow do wygenerowania oferty!");
        }

        Collections.shuffle(baseItems, random);

        List<ShopOfferEntity> offers = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_SHOP_ITEMS; i++) {
            BaseItemEntity baseItem = baseItems.get(i);

            ShopOfferEntity offer = new ShopOfferEntity(
                    null,
                    LocalDate.now(),
                    generateItemEntity(baseItem, character),
                    character
            );
            offers.add(offer);
        }

        return offers;
    }

    //tworzy item na podstawie basicItema z dynamicznymi statystykami, na podstawie aury itp, tutaj da sie algorytmy do liczenia tego
    private ItemEntity generateItemEntity(BaseItemEntity baseItemEntity, CharacterEntity character) {
        return new ItemEntity(
                null,
                //baseItemEntity.getName(),
                //baseItemEntity.getDescription(),
                //baseItemEntity.getSlotType(),
                baseItemEntity.getBaseRizz() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseStrength() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseAgility() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseEndurance() + random.nextInt(character.getAura()),
                baseItemEntity.getBaseLuck() + random.nextInt(character.getAura()),
                baseItemEntity.getBasePrice() + random.nextInt(character.getAura()),
                //baseItemEntity.getImagePath(),
                baseItemEntity
        );
    }

}
