package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.exception.InvalidItemTypeException;
import com.gametest.springprojekt.exception.InvalidSlotException;
import com.gametest.springprojekt.exception.ItemNotFoundException;
import com.gametest.springprojekt.model.BackpackItem;
import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.BaseItemRepository;
import com.gametest.springprojekt.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Service
public class ItemTokenService {
    private final BaseItemRepository baseItemRepository;
    private final ItemShopService itemShopService;
    private final ItemRepository itemRepository;
    private final SecureRandom random = new SecureRandom();

    private final double ITEM_DROP_CHANCE = 0.25;

    public ItemTokenService(BaseItemRepository baseItemRepository, ItemShopService itemShopService, ItemRepository itemRepository) {
        this.baseItemRepository = baseItemRepository;
        this.itemShopService = itemShopService;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ItemDto spendItemToken(CharacterEntity character, Long backpackTokenId) {
        BackpackItem backpackToken = character.getBackpack()
                .stream()
                .filter(item -> item.getId().equals(backpackTokenId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Nie znaleziono przedmiotu w plecaku!"
                ));

        BaseItemEntity token = backpackToken.getItem().getBaseItem();

        if (!token.getItemType().equals(ItemType.ITEM_TOKEN)) {
            throw new InvalidItemTypeException("Nie mozna wymienić tego przedmiotu!");
        }

        int tokenLuck = token.getBaseLuck();

        ItemEntity item = generateItemFromToken(character, tokenLuck);
        item.decreaseItemPrice();
        item = itemRepository.save(item);

        character.getBackpack().remove(backpackToken);
        character.addItemToBackpack(item);

        return item.generateItemDto();
    }

    public ItemEntity handleRewardToken() {
        List<BaseItemEntity> baseItems = baseItemRepository.findByItemType(ItemType.ITEM_TOKEN);

        if (baseItems.isEmpty()) {
            throw new ItemNotFoundException("Nie znaleziono przedmiotu!");
        }

        Collections.shuffle(baseItems, random);

        if (random.nextDouble() < ITEM_DROP_CHANCE) {
            ItemEntity token = generateTokenItemEntity(baseItems.getFirst());
            return itemRepository.save(token);
        }

        return null;
    }

    private ItemEntity generateItemFromToken(CharacterEntity character, int tokenLuck) {
        List<BaseItemEntity> baseItems = baseItemRepository.findBySlotTypeNot(SlotType.NONE);

        Collections.shuffle(baseItems, random);

        return itemShopService.generateItemEntity(baseItems.getFirst(), character, tokenLuck);
    }


    private ItemEntity generateTokenItemEntity(BaseItemEntity baseItemEntity) {
        return new ItemEntity(
                null,
                0,
                0,
                0,
                0,
                baseItemEntity.getBaseLuck(),
                0,
                baseItemEntity
        );
    }
}
