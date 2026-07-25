package com.gametest.springprojekt.model.mapper;

import com.gametest.springprojekt.dto.CharacterDto;
import com.gametest.springprojekt.model.CharacterEntity;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {
    public CharacterDto toDto(CharacterEntity character) {
        CharacterDto dto = new CharacterDto();

        dto.setName(character.getName());
        dto.setCharacterClass(character.getCharacterClass().getClassName());
        dto.setAuraLvl(character.getAuraLvl());
        if (character.getGang() != null) {
            dto.setGangName(character.getGang().getGangName());
        }
        return dto;
    }
}
