package com.sportlife.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utilidad para encriptación y verificación de contraseñas con BCrypt.
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
