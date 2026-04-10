package com.sportlife.core.services;

import com.sportlife.core.models.UserModel;

/**
 * Interfaz del servicio de autenticación: registro y login.
 */
public interface AuthService {
    UserModel register(UserModel userModel);
    String login(String email, String password);
}
