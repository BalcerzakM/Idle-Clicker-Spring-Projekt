package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.CharacterDto;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {
    public CharacterDto toDto(CharacterEntity character) {
        CharacterDto dto = new CharacterDto();

        dto.setName(character.getName());
        dto.setCharacterClass(character.getCharacterClass().getClassName());
        dto.setAuraLvl(character.getAuraLvl());

        return dto;
    }
}
