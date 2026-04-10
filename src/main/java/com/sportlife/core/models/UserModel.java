package com.sportlife.core.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo de dominio de usuario usado internamente por los servicios.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
}
