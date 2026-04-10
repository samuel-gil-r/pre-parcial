package com.sportlife.core.validators;

import com.sportlife.core.utils.exceptions.ConflictException;
import com.sportlife.core.utils.exceptions.ValidationException;
import com.sportlife.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validaciones de negocio para usuarios: email único, formato y contraseña.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateEmailUnique(String email) {
        log.debug("Validando unicidad de email: {}", email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("El email ya está registrado: " + email);
        }
    }

    public void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres");
        }
    }
}
