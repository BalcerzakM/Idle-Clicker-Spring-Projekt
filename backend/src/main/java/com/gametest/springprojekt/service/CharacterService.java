package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.dto.MoneyDto;
import com.gametest.springprojekt.exception.BackpackItemNotFoundException;
import com.gametest.springprojekt.exception.EquipmentItemNotFoundException;
import com.gametest.springprojekt.exception.InvalidSlotException;
import com.gametest.springprojekt.model.BackpackItem;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.EquipmentItem;
import com.gametest.springprojekt.model.ItemEntity;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.CharacterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
                character.getEquipment(),
                character.getBackpack()
                );
        return result;

    }

    public void equip(CharacterEntity character, Long equipmentItemId, Long backpackItemId) {
        //szukanie itemu w eq
        EquipmentItem equipmentItem = character.getEquipment().stream()
                .filter(e -> e.getId().equals(equipmentItemId))
                .findFirst()
                .orElseThrow(() -> new EquipmentItemNotFoundException(
                        "Przedmiot ekwipunku o id " + equipmentItemId + " nie istnieje"));
        //szukanie itemu w plecaku
        BackpackItem backpackItem = character.getBackpack().stream()
                .filter(b -> b.getId().equals(backpackItemId))
                .findFirst()
                .orElseThrow(() -> new BackpackItemNotFoundException(
                        "Przedmiot plecaka o id " + backpackItemId + " nie istnieje"));


        ItemEntity itemFromEquipment = equipmentItem.getItem();
        ItemEntity itemFromBackpack = backpackItem.getItem();

        //czy item z plecaka pasuje do slota
        SlotType slot = equipmentItem.getSlot();
        if (!itemFromBackpack.getBaseItem().getSlotType().equals(slot)) {
            throw new InvalidSlotException("Przedmiot nie może zostać założony w slocie " + slot);
        }

        //Zamiana przedmiotu w istniejącym EquipmentItem (bez usuwania encji)
        equipmentItem.setItem(itemFromBackpack);

        //Usuwamy BackpackItem z plecaka (orphanRemoval automatycznie go usunie z bazy)
        character.getBackpack().remove(backpackItem);

        //Dodajemy nowy BackpackItem ze zdjętym przedmiotem
        BackpackItem newBackpackItem = new BackpackItem(null, character, itemFromEquipment);
        character.getBackpack().add(newBackpackItem);
    }
}
