package com.sportlife.core.validators;

import com.sportlife.core.utils.exceptions.ConflictException;
import com.sportlife.core.utils.exceptions.ValidationException;
import com.sportlife.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validaciones de negocio para usuarios: campos obligatorios, email único, formato y contraseña.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateRegisterFields(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        validateEmailFormat(email);
        validatePasswordStrength(password);
    }

    public void validateLoginFields(String email, String password) {
        validateEmailFormat(email);
        if (password == null || password.isBlank()) {
            throw new ValidationException("La contraseña es obligatoria");
        }
    }

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

    private void validateEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("El email es obligatorio");
        }
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Formato de email inválido: " + email);
        }
    }
}
