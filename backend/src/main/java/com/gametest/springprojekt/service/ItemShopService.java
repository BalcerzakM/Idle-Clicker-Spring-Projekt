package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.exception.ItemNotFoundException;
import com.gametest.springprojekt.exception.NotEnoughAvailableBaseItemsException;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.BaseItemRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.ItemRepository;
import com.gametest.springprojekt.repository.ShopOfferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemShopService {
    private final SecureRandom random = new SecureRandom();
    private final ShopOfferRepository shopOfferRepository;
    private final BaseItemRepository baseItemRepository;
    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final CalculationService calculationService;

    private final int NUMBER_OF_SHOP_ITEMS = 4;

    //sciaga dzisiejsza oferte z bazy danych
    @Transactional
    public List<ItemDto> getTodayShopItemDto(CharacterEntity character) {
        LocalDate today = LocalDate.now();

        List<ShopOfferEntity> offers = shopOfferRepository.findByCharacterAndOfferDate(character, today);

        if(offers.isEmpty()) {
            offers = shopOfferRepository.saveAll(generateRandomShopOffers(character));
        }

        List<ItemDto> itemDtos = new ArrayList<>();

        for (ShopOfferEntity offerEntity : offers) {
            itemDtos.add(generateShopItemDto(offerEntity));
        }

        return itemDtos;
    }

    //kupowanie itemow, odejmuje pieniadze, usuwa oferte, generuje nowa i dodaje item do plecaka
    @Transactional
    public ItemDto buyItemFromOffer(CharacterEntity character, Long shopOfferId) {
        ShopOfferEntity shopOffer = shopOfferRepository.findById(shopOfferId)
                .orElseThrow(() -> new ItemNotFoundException("Nie znaleziono przedmiotu o podanym ID"));

        ItemEntity boughtItem = shopOffer.getItem();

        if (character.getMoney() < boughtItem.getPrice()) {
            throw new InsufficientMoneyException("Gracz ma za malo pieniedzy!");
        }

        ItemDto itemDto = generateShopItemDto(shopOffer);

        character.setMoney(character.getMoney() - shopOffer.getItem().getPrice());

        shopOffer.setItem(null);
        shopOfferRepository.delete(shopOffer);

        boughtItem.decreaseItemPrice();

        character.addItemToBackpack(boughtItem);

        shopOfferRepository.save(generateRandomShopOffer(character));

        return itemDto;
    }

    @Transactional
    public void sellItem(CharacterEntity character, Long backpackItemId) {
        BackpackItem backpackItem = character.getBackpack()
                .stream()
                .filter(item -> item.getId().equals(backpackItemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Nie znaleziono przedmiotu w plecaku!"
                ));

        ItemEntity item = backpackItem.getItem();

        character.setMoney(character.getMoney() + item.getPrice());

        character.getBackpack().remove(backpackItem);
    }

    //generuje dto na podstawie oferty sciagnietej z bazy
    public ItemDto generateShopItemDto(ShopOfferEntity shopOfferEntity) {
        ItemEntity item = shopOfferEntity.getItem();
        return new ItemDto(
                shopOfferEntity.getId(),
                item.getBaseItem().getName(),
                item.getBaseItem().getDescription(),
                item.getBaseItem().getItemType(),
                item.getBaseItem().getSlotType(),
                item.getTotalRizz(),
                item.getTotalStrength(),
                item.getTotalAgility(),
                item.getTotalEndurance(),
                item.getTotalLuck(),
                item.getPrice(),
                item.getBaseItem().getImagePath()
        );
    }

    //o podanej godzinie odswieza oferte dla kazdego charactera w bazie
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void refreshShopOffers() {
        List<CharacterEntity> characters = characterRepository.findAll();

        for (CharacterEntity character : characters) {
            refreshCharacterShopOffer(character);
        }
    }

    //to do odswiezanai dla uzytkownika za krysztaly
    @Transactional
    public void refreshShopOfferForCrystals(CharacterEntity character) {
        if (character.getCristals() <= 0) {
            throw new InsufficientMoneyException("Gracz ma za malo krysztalow!");
        }

        character.setCristals(character.getCristals() - 1);

        refreshCharacterShopOffer(character);
    }

    private void refreshCharacterShopOffer(CharacterEntity character) {
        List<ShopOfferEntity> oldOffers = shopOfferRepository.findByCharacter(character);

        List<ItemEntity> oldItems = oldOffers.stream()
                .map(ShopOfferEntity::getItem)
                .toList();

        shopOfferRepository.deleteAll(oldOffers);
        shopOfferRepository.flush();

        itemRepository.deleteAll(oldItems);

        shopOfferRepository.saveAll(generateRandomShopOffers(character));
    }

    //generuje jeden item do oferty bez powtarzajacych sie baseItem, uzywana po np. kupieniu itema do uzupelnienia oferty
    private ShopOfferEntity generateRandomShopOffer(CharacterEntity character) {
        List<ShopOfferEntity> presentOffers = shopOfferRepository.findByCharacterAndOfferDate(character, LocalDate.now());

        Set<Long> usedBaseItemIds = presentOffers.stream().map(offer -> offer
                .getItem()
                .getBaseItem()
                .getId()).collect(Collectors.toSet());

        List<BaseItemEntity> baseItems = baseItemRepository.findBySlotTypeNot(SlotType.NONE);

        List<BaseItemEntity> availableBaseItems = baseItems.stream()
                .filter(item -> !usedBaseItemIds
                        .contains(item.getId())).toList();

        if (availableBaseItems.isEmpty()) {
            throw new NotEnoughAvailableBaseItemsException("Brak dostepnych base itemow do wygenerowania oferty!");
        }

        BaseItemEntity basicItem = availableBaseItems.get(random.nextInt(availableBaseItems.size()));

        ItemEntity item = generateItemEntity(basicItem, character, 0);

        return new ShopOfferEntity(null, LocalDate.now(), item, character);
    }

    //tworzy full zestaw oferty, oddzielna metoda by zapobiec duplikatom, ta jest uzywana do pelnego refresha ofert
    private List<ShopOfferEntity> generateRandomShopOffers(CharacterEntity character) {
        List<BaseItemEntity> baseItems = baseItemRepository.findBySlotTypeNot(SlotType.NONE);

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
                    generateItemEntity(baseItem, character, 0),
                    character
            );
            offers.add(offer);
        }

        return offers;
    }

    //tworzy item na podstawie basicItema z dynamicznymi statystykami, na podstawie aury itp, tutaj da sie algorytmy do liczenia tego
    public ItemEntity generateItemEntity(BaseItemEntity baseItem, CharacterEntity character, int amplifier) {
        int bonusRizz = calculationService.calculateItemBonusRizz(baseItem, character, amplifier);
        int bonusStrength = calculationService.calculateItemBonusStrength(baseItem, character, amplifier);
        int bonusAgility = calculationService.calculateItemBonusAgility(baseItem, character, amplifier);
        int bonusEndurance = calculationService.calculateItemBonusEndurance(baseItem, character, amplifier);
        int bonusLuck = calculationService.calculateItemBonusLuck(baseItem, character, amplifier);

        int statsSum = bonusRizz + bonusStrength + bonusAgility + bonusEndurance + bonusLuck;

        return new ItemEntity(
                null,
                bonusRizz,
                bonusStrength,
                bonusAgility,
                bonusEndurance,
                bonusLuck,
                calculationService.calculateItemPrice(baseItem, character, statsSum),
                baseItem
        );
    }

}
