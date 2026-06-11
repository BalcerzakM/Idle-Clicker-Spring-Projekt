package com.gametest.springprojekt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportDto {
    private String userId;
    @NotBlank(message = "To pole nie może być puste")
    private String title;
    @NotBlank(message = "To pole nie może być puste")
    private String description;
}
