package com.gametest.springprojekt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwapRequestDto {
    private Long equipmentItemId; //może być puste
    @NotNull
    private Long backpackItemId;
}
