package com.sportlife.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO de salida con el token JWT tras autenticación exitosa.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }
}
