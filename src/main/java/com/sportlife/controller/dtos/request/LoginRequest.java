package com.sportlife.controller.dtos.request;

import lombok.Data;

/**
 * DTO de entrada para autenticación de usuario.
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
