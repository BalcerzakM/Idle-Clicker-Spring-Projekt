package com.gametest.springprojekt.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDto {

    @NotBlank(message = "To pole nie może być puste")
    @Size(min=5, max=20, message = "Login musi zawierać od 5 do 20 znaków")
    private String username;

    @NotBlank(message = "To pole nie może być puste")
    @Email(message = "Nieprawidłowy format e-mail")
    private String email;

    @NotBlank(message = "To pole nie może być puste")
    @Size(min=8, message = "Hasło musi mieć co najmniej 8 znaków")
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*\\d).*$", message = "Hasło musi zawierać minimum jedną wielką literę i cyfrę")
    private String password;

    @NotBlank(message="Musisz powtórzyć hasło")
    private String confirmPassword;
}
