package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.EffectDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.EffectEntity;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.mapper.ItemMapper;
import com.gametest.springprojekt.repository.EffectRepository;
import com.gametest.springprojekt.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EffectService {
    private final EffectRepository effectRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public void validateAndRemoveEffects(CharacterEntity character) {
        Instant now = Instant.now();

        List<EffectEntity> toRemoveEffects = new ArrayList<>();
        List<ItemEntity> toRemoveItems = new ArrayList<>();

        for (EffectEntity effect : character.getEffects()) {
            if (!effect.getEffectEndTime().isAfter(now)) {
                toRemoveEffects.add(effect);
                toRemoveItems.add(effect.getItem());
            }
        }

        character.getEffects().removeAll(toRemoveEffects);

        effectRepository.deleteAll(toRemoveEffects);
        itemRepository.deleteAll(toRemoveItems);
    }

    public EffectEntity createEffect(CharacterEntity character, ItemEntity drinkItem) {
        Instant startTime = Instant.now();
        return new EffectEntity(
                null,
                character,
                drinkItem,
                startTime,
                startTime.plusSeconds(drinkItem.getBaseItem().getDurationInSeconds())
        );
    }

    public EffectDto generateEffectDto(EffectEntity effect) {
        return new EffectDto(
                itemMapper.toDto(effect.getItem()),
                effect.getEffectStartTime(),
                effect.getEffectEndTime()
        );
    }
}
