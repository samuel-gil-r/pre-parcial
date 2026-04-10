package com.sportlife.controller.dtos.request;

import lombok.Data;

/**
 * DTO de entrada para el registro de un nuevo usuario.
 */
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
