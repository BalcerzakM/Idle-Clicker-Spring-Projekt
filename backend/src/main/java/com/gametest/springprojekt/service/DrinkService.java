package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.EffectDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.BackpackItemNotFoundException;
import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.exception.InvalidItemTypeException;
import com.gametest.springprojekt.exception.ItemNotFoundException;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.mapper.ItemMapper;
import com.gametest.springprojekt.repository.BaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final BaseItemRepository baseItemRepository;
    private final CalculationService calculationService;
    private final ItemMapper itemMapper;


    public List<ItemDto> getDrinkOffers(CharacterEntity character) {
        List<BaseItemEntity> baseDrinks = baseItemRepository.findByItemType(ItemType.DRINK);

        List<ItemDto> itemDtos = new ArrayList<>();

        for (BaseItemEntity baseDrink : baseDrinks) {
            itemDtos.add(new ItemDto(
                baseDrink.getId(),
                baseDrink.getName(),
                baseDrink.getDescription(),
                baseDrink.getItemType(),
                baseDrink.getSlotType(),
                calculationService.calculateDrinkValue(baseDrink.getBaseRizz(), character),
                calculationService.calculateDrinkValue(baseDrink.getBaseStrength(), character),
                calculationService.calculateDrinkValue(baseDrink.getBaseAgility(), character),
                calculationService.calculateDrinkValue(baseDrink.getBaseEndurance(), character),
                calculationService.calculateDrinkValue(baseDrink.getBaseLuck(), character),
                calculationService.calculateDrinkValue(baseDrink.getBasePrice(), character),
                baseDrink.getImagePath()
            ));
        }

        return itemDtos;
    }

    @Transactional
    public ItemDto buyDrink(CharacterEntity character, Long drinkId) {
        BaseItemEntity baseDrink = baseItemRepository.findById(drinkId).orElseThrow(
                ()-> new ItemNotFoundException("Nie znaleziono napoju o podanym ID")
        );

        int drinkPrice = calculationService.calculateDrinkValue(baseDrink.getBasePrice(), character);

        if (character.getMoney() < drinkPrice) {
            throw new InsufficientMoneyException("Gracz ma za mało pieniędzy!");
        }

        character.setMoney(character.getMoney() - drinkPrice);

        ItemEntity drinkItem = generateDrinkItemFromBaseItem(baseDrink, character);

        drinkItem.decreaseItemPrice();

        character.addItemToBackpack(drinkItem);

        return itemMapper.toDto(drinkItem);

    }

    private ItemEntity generateDrinkItemFromBaseItem(BaseItemEntity baseDrink, CharacterEntity character) {
        return new ItemEntity(
            null,
            calculationService.calculateDrinkValue(baseDrink.getBaseRizz(), character),
            calculationService.calculateDrinkValue(baseDrink.getBaseStrength(), character),
            calculationService.calculateDrinkValue(baseDrink.getBaseAgility(), character),
            calculationService.calculateDrinkValue(baseDrink.getBaseEndurance(), character),
            calculationService.calculateDrinkValue(baseDrink.getBaseLuck(), character),
            calculationService.calculateDrinkValue(baseDrink.getBasePrice(), character),
            baseDrink
        );
    }

    @Transactional
    public EffectDto handleUseDrink(CharacterEntity character, Long backpackItemId) {
         BackpackItem backpackItem = character.getBackpack().stream()
                 .filter(b -> b.getId().equals(backpackItemId))
                 .findFirst()
                 .orElseThrow(() -> new BackpackItemNotFoundException("Przedmiot plecaka o id " + backpackItemId + " nie istnieje"));

         ItemEntity itemFromBackpack = backpackItem.getItem();

         if (!itemFromBackpack.getBaseItem().getItemType().equals(ItemType.DRINK)) {
             throw new InvalidItemTypeException("Nieprawidłowy typ przedmiotu");
         }

         EffectEntity effect = createEffect(character, itemFromBackpack);

         character.addEffect(effect);

         character.getBackpack().remove(backpackItem);

         return generateEffectDto(effect);
    }

    private EffectEntity createEffect(CharacterEntity character, ItemEntity drinkItem) {
        Instant startTime = Instant.now();
        return new EffectEntity(
         null,
            character,
            drinkItem,
            startTime,
            startTime.plusSeconds(drinkItem.getBaseItem().getDurationInSeconds())
        );
    }

    private EffectDto generateEffectDto(EffectEntity effect) {
        return new EffectDto(
                itemMapper.toDto(effect.getItem()),
                effect.getEffectStartTime(),
                effect.getEffectEndTime()
        );
    }


}
