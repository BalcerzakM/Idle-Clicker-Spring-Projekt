package com.gametest.springprojekt.service;

import com.gametest.springprojekt.model.ActiveVehicleEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final CharacterRepository characterRepository;

    @Transactional
    public void validateAndRemoveExpiredVehicle(Long characterId) {
        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono postaci o podanym ID!"));

        ActiveVehicleEntity activeVehicle = character.getActiveVehicle();

        if (activeVehicle != null && activeVehicle.isExpired()) {
            character.setActiveVehicle(null);
        }
    }
}
