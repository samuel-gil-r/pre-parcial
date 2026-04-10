package com.sportlife.controller.dtos.response;

import com.sportlife.core.models.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de salida con datos del usuario (sin contraseña).
 */
@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
