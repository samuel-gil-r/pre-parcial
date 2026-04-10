package com.sportlife.core.services;

import com.sportlife.core.models.Role;
import com.sportlife.core.models.UserModel;
import com.sportlife.core.utils.JwtUtil;
import com.sportlife.core.utils.PasswordUtil;
import com.sportlife.core.utils.exceptions.ResourceNotFoundException;
import com.sportlife.core.utils.exceptions.ValidationException;
import com.sportlife.core.validators.UserValidator;
import com.sportlife.persistence.mappers.UserPersistenceMapper;
import com.sportlife.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de autenticación con validación, cifrado y JWT.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final UserValidator userValidator;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserModel register(UserModel userModel) {
        log.info("Registrando usuario: {}", userModel.getEmail());
        userValidator.validateRegisterFields(userModel.getName(), userModel.getEmail(), userModel.getPassword());
        userValidator.validateEmailUnique(userModel.getEmail());

        userModel.setPassword(passwordUtil.encode(userModel.getPassword()));
        userModel.setRole(Role.USER);

        var entity = userPersistenceMapper.toEntity(userModel);
        var saved = userRepository.save(entity);
        log.info("Usuario registrado con id: {}", saved.getId());
        return userPersistenceMapper.toModel(saved);
    }

    @Override
    public String login(String email, String password) {
        log.info("Intento de login: {}", email);
        userValidator.validateLoginFields(email, password);
        var entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        if (!passwordUtil.matches(password, entity.getPassword())) {
            throw new ValidationException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(entity.getEmail(), entity.getRole().name());
        log.info("Login exitoso para: {}", email);
        return token;
    }
}
