package com.sportlife.controller.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para el registro de un nuevo usuario.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
