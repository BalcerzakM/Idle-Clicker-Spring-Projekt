package com.gametest.springprojekt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharacterCreatorDto {

    @NotBlank(message = "To pole nie może być puste")
    @Size(min=5, max=20, message = "Nazwa postaci musi zawierać od 5 do 20 znaków!")
    private String name;

    @NotNull(message = "Musisz wybrać klasę postaci")
    private String characterClass;

    @NotBlank(message = "To pole nie może być puste")
    private String avatarPicture;
}
