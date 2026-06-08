package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.model.BaseVehicleEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    private final CharacterService characterService;

    @GetMapping
    public List<BaseVehicleEntity> getAvailableVehicles() {
        return vehicleService.getAllBaseVehicles();
    }

    @PostMapping("/{baseVehicleId}/rent")
    public void rentVehicle(
            @PathVariable Long baseVehicleId,
            @RequestParam int days) {
        CharacterEntity character = characterService.getCurrentCharacter();
        vehicleService.rentVehicle(character, baseVehicleId, days);
    }

    @DeleteMapping("/cancel")
    public void cancelVehicle() {
        CharacterEntity character = characterService.getCurrentCharacter();
        vehicleService.cancelActiveVehicle(character);
    }


}
