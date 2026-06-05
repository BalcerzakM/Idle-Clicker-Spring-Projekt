package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CharacterCreatorDto;
import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.dto.ItemsAndStatsDto;
import com.gametest.springprojekt.exception.*;
import com.gametest.springprojekt.dto.ShortCharacterInfoDto;
import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.model.enums.StatName;
import com.gametest.springprojekt.repository.CharacterClassRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final CharacterClassRepository characterClassRepository;

    public CharacterEntity getCurrentCharacter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika!"));

        List<CharacterEntity> characters = user.getCharacters();
        if (characters.isEmpty()) {
            throw new CharacterNotFoundException("Użytkownik nie posiada jeszcze żadnej postaci!");
        }
        // na razie na sztywno, z listy pierwsza postać po prostu
        return user.getCharacters().getFirst();
    }


    @Transactional
    public void createAndAssignCharacter(CharacterCreatorDto dto, String username) {
        UserEntity user = userRepository.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        if (characterRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Ta nazwa postaci jest już zajęta!");
        }

        CharacterClassEntity chosenClass = characterClassRepository.findByClassName(dto.getCharacterClass())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Błąd: Wybrana klasa postaci '" + dto.getCharacterClass() + "' nie istnieje!"
                ));

        CharacterEntity newCharacter = new CharacterEntity();
        newCharacter.setUser(user);
        newCharacter.setName(dto.getName());
        newCharacter.setCharacterClass(chosenClass);
        newCharacter.setAvatarPicture(dto.getAvatarPicture());

        newCharacter.setMoney(chosenClass.getBaseMoney());
        newCharacter.setCristals(0);
        newCharacter.setAuraLvl(1);
        newCharacter.setAura(0);
        newCharacter.setRizz(chosenClass.getBaseRizz());
        newCharacter.setStrength(chosenClass.getBaseStrength());
        newCharacter.setAgility(chosenClass.getBaseAgility());
        newCharacter.setEndurance(chosenClass.getBaseEndurance());
        newCharacter.setLuck(chosenClass.getBaseLuck());

        characterRepository.save(newCharacter);
    }

    public ShortCharacterInfoDto getShortCharacterInfo(CharacterEntity character) {
        character.updateAuraLevel();

        int currentLevelAuraRequirement = (character.getAuraLvl() - 1) * (character.getAuraLvl() - 1) * 100;

        int nextLevelAuraRequirement = character.getAuraLvl() * character.getAuraLvl() * 100;

        int levelProgressPercent = (int) (((character.getAura() - currentLevelAuraRequirement) * 100) / (nextLevelAuraRequirement - currentLevelAuraRequirement));

        return new ShortCharacterInfoDto(
                character.getMoney(),
                character.getCristals(),
                character.getAvatarPicture(),
                character.getAura(),
                character.getAuraLvl(),
                nextLevelAuraRequirement,
                levelProgressPercent
        );
    }

    @Transactional(readOnly = true) // bo klika pól, które odczytuje ma leniwego fetcha
    public ItemsAndStatsDto getItemsAndStats(CharacterEntity character) {
        Map<String, Integer> stats = character.getEquipmentStatsSum();
        return new ItemsAndStatsDto(
                character.getName(),
                character.getAvatarPicture(),
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

    }

    //dodane transactional bo nie zapisywalo wczesniej i nie dodawalo do eq
    @Transactional
    public void equip(CharacterEntity character, Long equipmentItemId, Long backpackItemId) {
        //szukanie itemu w plecaku
        BackpackItem backpackItem = character.getBackpack().stream()
                .filter(b -> b.getId().equals(backpackItemId))
                .findFirst()
                .orElseThrow(() -> new BackpackItemNotFoundException(
                        "Przedmiot plecaka o id " + backpackItemId + " nie istnieje"));

        ItemEntity itemFromBackpack = backpackItem.getItem();

        if (itemFromBackpack.getBaseItem().getSlotType().equals(SlotType.NONE)) {
            throw new InvalidSlotException("Nie mozna zalozyc tego przedmiotu!");
        }

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
                    backpackItem.getItem().getBaseItem().getItemType(),
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

    private List<ItemDto> equipmentItemToItemDtos(List<EquipmentItem> equipmentItems) {
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
                    equipmentItem.getItem().getBaseItem().getImagePath()
            ));
        }
        return itemDtos;
    }


    @Transactional
    public void incrementStat(CharacterEntity character, StatName stat, int amount) {
        if (character.getCristals() <= 0) {
            throw new InsufficientMoneyException("Gracz ma za malo krysztalow!");
        }

        character.setCristals(character.getCristals() - 1);

        switch (stat) {
            case RIZZ:      character.setRizz(character.getRizz() + amount); break;
            case STRENGTH:  character.setStrength(character.getStrength() + amount); break;
            case AGILITY:   character.setAgility(character.getAgility() + amount); break;
            case ENDURANCE: character.setEndurance(character.getEndurance() + amount); break;
            case LUCK:      character.setLuck(character.getLuck() + amount); break;
        }
    }

}
