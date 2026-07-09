package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.exception.ItemNotFoundException;
import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.mapper.ItemMapper;
import com.gametest.springprojekt.repository.BaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //narazie cena na sztywno
        if (character.getMoney() < calculationService.calculateDrinkValue(baseDrink.getBasePrice(), character)) {
            throw new InsufficientMoneyException("Gracz ma za mało pieniędzy!");
        }

        character.setMoney(character.getMoney() - baseDrink.getBasePrice());

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


}
