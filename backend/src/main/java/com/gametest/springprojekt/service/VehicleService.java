package com.gametest.springprojekt.service;

import com.gametest.springprojekt.exception.InsufficientMoneyException;
import com.gametest.springprojekt.exception.VehicleIsAlreadyRentedException;
import com.gametest.springprojekt.model.ActiveVehicleEntity;
import com.gametest.springprojekt.model.BaseVehicleEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.repository.BaseVehicleRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final CharacterRepository characterRepository;
    private final BaseVehicleRepository baseVehicleRepository;

    @Transactional
    public void validateAndRemoveExpiredVehicle(CharacterEntity character) {
        ActiveVehicleEntity activeVehicle = character.getActiveVehicle();

        if (activeVehicle != null && activeVehicle.isExpired()) {
            character.setActiveVehicle(null);
        }

        characterRepository.save(character);
    }

    public List<BaseVehicleEntity> getAllBaseVehicles() {
        return baseVehicleRepository.findAll();
    }

    @Transactional
    public void rentVehicle(CharacterEntity character, Long baseVehicleId , int rentDays) {
        ActiveVehicleEntity currentVehicle = character.getActiveVehicle();
        if (currentVehicle != null) {
            throw new VehicleIsAlreadyRentedException("Już masz wynajęty pojazd! Anuluj swój obecny pojazd aby móc kupić inny.");
        }

        if (rentDays <= 0) {
            throw new IllegalArgumentException("Wynajem musi trwać przynajmniej 1 dzień!");
        }

        BaseVehicleEntity baseVehicle = baseVehicleRepository.findById(baseVehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie ma takiego pojazdu na parkingu"));

        int finalPrice = baseVehicle.getPrice() * rentDays;
        if (character.getCristals() < finalPrice) {
            throw new InsufficientMoneyException("Nie masz wystarczająco kryształu");
        }

        character.setCristals(character.getCristals() - finalPrice);
        long durationSeconds = (long) rentDays * 24L * 60L * 60L;

        ActiveVehicleEntity newRental = new ActiveVehicleEntity();
        newRental.setBaseVehicle(baseVehicle);
        newRental.setExpiryTime(Instant.now().plusSeconds(durationSeconds));
        character.setActiveVehicle(newRental);

        characterRepository.save(character);
    }

    @Transactional
    public void cancelActiveVehicle(CharacterEntity character) {
        if(character.getActiveVehicle() == null) {
            throw new IllegalStateException("Nie posiadasz obecnie żadnego wynajętego pojazdu!");
        }
        character.setActiveVehicle(null);
        characterRepository.save(character);
    }
}
