package com.gametest.springprojekt.model.mapper;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.ItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
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
                item.getBaseItem().getImagePath()
        );
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
