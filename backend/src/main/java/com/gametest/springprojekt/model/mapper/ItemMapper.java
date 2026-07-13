package com.gametest.springprojekt.model.mapper;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final CalculationService calculationService;

    public ItemDto toDto(ItemEntity item) {
        return new ItemDto(
                item.getId(),
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
                item.getBaseItem().getImagePath(),
                item.getBaseItem().getDurationInSeconds(),
                item.getBaseItem().getEffectType(),
                item.getBaseItem().getEffectValue(),
                item.getBaseItem().isPremium()
        );
    }

    //generuje dto na podstawie oferty sciagnietej z bazy
    public ItemDto shopOfferToItemDto(ShopOfferEntity shopOfferEntity) {
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
                item.getBaseItem().getImagePath(),
                item.getBaseItem().getDurationInSeconds(),
                item.getBaseItem().getEffectType(),
                item.getBaseItem().getEffectValue(),
                item.getBaseItem().isPremium()
        );
    }

    public ItemDto baseDrinkToItemDto(BaseItemEntity baseDrink, CharacterEntity character) {
        return new ItemDto(
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
                baseDrink.getImagePath(),
                baseDrink.getDurationInSeconds(),
                baseDrink.getEffectType(),
                baseDrink.getEffectValue(),
                baseDrink.isPremium()
        );
    }

    public List<ItemDto> backpackItemsToItemDtos(List<BackpackItem> backpackItems) {
        List<ItemDto> itemDtos = new ArrayList<>();

        for(BackpackItem backpackItem : backpackItems) {
            itemDtos.add(new ItemDto(
                    backpackItem.getId(),
                    backpackItem.getItem().getBaseItem().getName(),
                    backpackItem.getItem().getBaseItem().getDescription(),
                    backpackItem.getItem().getBaseItem().getItemType(),
                    backpackItem.getItem().getBaseItem().getSlotType(),
                    backpackItem.getItem().getTotalRizz(),
                    backpackItem.getItem().getTotalStrength(),
                    backpackItem.getItem().getTotalAgility(),
                    backpackItem.getItem().getTotalEndurance(),
                    backpackItem.getItem().getTotalLuck(),
                    backpackItem.getItem().getPrice(),
                    backpackItem.getItem().getBaseItem().getImagePath(),
                    backpackItem.getItem().getBaseItem().getDurationInSeconds(),
                    backpackItem.getItem().getBaseItem().getEffectType(),
                    backpackItem.getItem().getBaseItem().getEffectValue(),
                    backpackItem.getItem().getBaseItem().isPremium()
            ));
        }
        return itemDtos;
    }

    public List<ItemDto> equipmentItemsToItemDtos(List<EquipmentItem> equipmentItems) {
        List<ItemDto> itemDtos = new ArrayList<>();

        for(EquipmentItem equipmentItem : equipmentItems) {
            itemDtos.add(new ItemDto(
                    equipmentItem.getId(),
                    equipmentItem.getItem().getBaseItem().getName(),
                    equipmentItem.getItem().getBaseItem().getDescription(),
                    equipmentItem.getItem().getBaseItem().getItemType(),
                    equipmentItem.getItem().getBaseItem().getSlotType(),
                    equipmentItem.getItem().getTotalRizz(),
                    equipmentItem.getItem().getTotalStrength(),
                    equipmentItem.getItem().getTotalAgility(),
                    equipmentItem.getItem().getTotalEndurance(),
                    equipmentItem.getItem().getTotalLuck(),
                    equipmentItem.getItem().getPrice(),
                    equipmentItem.getItem().getBaseItem().getImagePath(),
                    equipmentItem.getItem().getBaseItem().getDurationInSeconds(),
                    equipmentItem.getItem().getBaseItem().getEffectType(),
                    equipmentItem.getItem().getBaseItem().getEffectValue(),
                    equipmentItem.getItem().getBaseItem().isPremium()
            ));
        }
        return itemDtos;
    }

//    public ItemDto baseItemToDto(BaseItemEntity item) {
//        return new ItemDto(
//                item.getId(),
//                item.getName(),
//                item.getDescription(),
//                item.getItemType(),
//                item.getSlotType(),
//                item.getBaseRizz(),
//                item.getBaseStrength(),
//                item.getBaseAgility(),
//                item.getBaseEndurance(),
//                item.getBaseLuck(),
//                item.getBasePrice(),
//                item.getImagePath()
//        );
//    }
}
