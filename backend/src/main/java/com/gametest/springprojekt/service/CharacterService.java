package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.MoneyDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public MoneyDto getMoney(CharacterEntity character) {
        return new MoneyDto(character.getMoney(), character.getCristals());
    }
}
