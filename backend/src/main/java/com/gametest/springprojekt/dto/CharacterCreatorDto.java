package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.enums.CharacterClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharacterCreatorDto {

    @NotBlank(message = "To pole nie może być puste")
    @Size(min=4, max=20, message = "Login musi zawierać od 4 do 20 znaków")
    private String name;

    @NotNull(message = "Musisz wybrać klasę postaci")
    private CharacterClass characterClass;

    @NotBlank(message = "To pole nie może być puste")
    private String avatarPicture;
}
