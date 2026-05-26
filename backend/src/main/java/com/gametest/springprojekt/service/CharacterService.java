package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.dto.MoneyDto;
import com.gametest.springprojekt.exception.BackpackItemNotFoundException;
import com.gametest.springprojekt.exception.EquipmentItemNotFoundException;
import com.gametest.springprojekt.exception.InvalidSlotException;
import com.gametest.springprojekt.exception.SlotAlreadyOccupiedException;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CharacterService {
    CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }


    public MoneyDto getMoney(CharacterEntity character) {
        return new MoneyDto(character.getMoney(), character.getCristals());
    }

    @Transactional(readOnly = true) // bo klika pól, które odczytuje ma leniwego fetcha
    public ItemsAndStatsDto getItemsAndStats(CharacterEntity character) {
        Map<String, Integer> stats = character.getEquipmentStatsSum();
        ItemsAndStatsDto result = new ItemsAndStatsDto(
                character.getName(),
                character.getAuraLvl(),
                character.getAura(),
                stats.get("rizz"),
                stats.get("strength"),
                stats.get("agility"),
                stats.get("endurance"),
                stats.get("luck"),
                equipmentItemToItemDtos(character.getEquipment()),
                backpackItemToItemDtos(character.getBackpack())
                );
        return result;

    }

    public void equip(CharacterEntity character, Long equipmentItemId, Long backpackItemId) {
        //szukanie itemu w plecaku
        BackpackItem backpackItem = character.getBackpack().stream()
                .filter(b -> b.getId().equals(backpackItemId))
                .findFirst()
                .orElseThrow(() -> new BackpackItemNotFoundException(
                        "Przedmiot plecaka o id " + backpackItemId + " nie istnieje"));

        ItemEntity itemFromBackpack = backpackItem.getItem();

        if(equipmentItemId == null){
            SlotType targetSlot = itemFromBackpack.getBaseItem().getSlotType();
            boolean slotOccupied = character.getEquipment().stream()
                    .anyMatch(e -> e.getSlot() == targetSlot);
            if (slotOccupied) {
                throw new SlotAlreadyOccupiedException(
                        "Slot " + targetSlot + " jest już zajęty. Użyj zamiany (swap).");
            }
            character.getBackpack().remove(backpackItem);

            EquipmentItem newEq = new EquipmentItem();
            newEq.setSlot(targetSlot);
            newEq.setItem(itemFromBackpack);
            newEq.setPlayer(character);

            character.getEquipment().add(newEq);
        }
        else {
            //szukanie itemu w eq
            EquipmentItem equipmentItem = character.getEquipment().stream()
                    .filter(e -> e.getId().equals(equipmentItemId))
                    .findFirst()
                    .orElseThrow(() -> new EquipmentItemNotFoundException(
                            "Przedmiot ekwipunku o id " + equipmentItemId + " nie istnieje"));


            ItemEntity itemFromEquipment = equipmentItem.getItem();

            //czy item z plecaka pasuje do slota
            SlotType slot = equipmentItem.getSlot();
            if (!itemFromBackpack.getBaseItem().getSlotType().equals(slot)) {
                throw new InvalidSlotException("Przedmiot nie może zostać założony w slocie " + slot);
            }

            //Zamiana przedmiotu w istniejącym EquipmentItem (bez usuwania encji)
            equipmentItem.setItem(itemFromBackpack);

            //Usuwamy BackpackItem z plecaka (orphanRemoval automatycznie go usunie z bazy)
            character.getBackpack().remove(backpackItem);

            //Dodajemy nowy BackpackItem (zdjęty przedmiot)
            BackpackItem newBackpackItem = new BackpackItem(null, character, itemFromEquipment);
            character.getBackpack().add(newBackpackItem);
        }
    }

    private List<ItemDto> backpackItemToItemDtos(List<BackpackItem> backpackItems) {
        List<ItemDto> itemDtos = new ArrayList<>();

        for(BackpackItem backpackItem : backpackItems) {
            itemDtos.add(new ItemDto(
                    backpackItem.getId(),
                    backpackItem.getItem().getBaseItem().getName(),
                    backpackItem.getItem().getBaseItem().getDescription(),
                    backpackItem.getItem().getBaseItem().getSlotType(),
                    backpackItem.getItem().getTotalRizz(),
                    backpackItem.getItem().getTotalStrength(),
                    backpackItem.getItem().getTotalAgility(),
                    backpackItem.getItem().getTotalEndurance(),
                    backpackItem.getItem().getTotalLuck(),
                    backpackItem.getItem().getPrice(),
                    backpackItem.getItem().getBaseItem().getImagePath()
            ));
        }
        return itemDtos;
    }

    private List<ItemDto> equipmentItemToItemDtos(Set<EquipmentItem> equipmentItems) {
        List<ItemDto> itemDtos = new ArrayList<>();

        for(EquipmentItem equipmentItem : equipmentItems) {
            itemDtos.add(new ItemDto(
                    equipmentItem.getId(),
                    equipmentItem.getItem().getBaseItem().getName(),
                    equipmentItem.getItem().getBaseItem().getDescription(),
                    equipmentItem.getItem().getBaseItem().getSlotType(),
                    equipmentItem.getItem().getTotalRizz(),
                    equipmentItem.getItem().getTotalStrength(),
                    equipmentItem.getItem().getTotalAgility(),
                    equipmentItem.getItem().getTotalEndurance(),
                    equipmentItem.getItem().getTotalLuck(),
                    equipmentItem.getItem().getPrice(),
                    equipmentItem.getItem().getBaseItem().getImagePath()
            ));
        }
        return itemDtos;
    }
}
