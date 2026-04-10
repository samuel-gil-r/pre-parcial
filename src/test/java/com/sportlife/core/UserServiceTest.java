package com.sportlife.core;

import com.sportlife.core.models.Role;
import com.sportlife.core.models.UserModel;
import com.sportlife.core.services.AuthServiceImpl;
import com.sportlife.core.utils.JwtUtil;
import com.sportlife.core.utils.PasswordUtil;
import com.sportlife.core.utils.exceptions.ConflictException;
import com.sportlife.core.validators.UserValidator;
import com.sportlife.persistence.entities.UserEntity;
import com.sportlife.persistence.mappers.UserPersistenceMapper;
import com.sportlife.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de autenticación con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserPersistenceMapper userPersistenceMapper;
    @Mock private UserValidator userValidator;
    @Mock private PasswordUtil passwordUtil;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_success_whenEmailIsNew() {
        var model = UserModel.builder()
                .name("Ana García")
                .email("ana@test.com")
                .password("securePass1")
                .build();

        var entity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Ana García")
                .email("ana@test.com")
                .password("hashed")
                .role(Role.USER)
                .build();

        doNothing().when(userValidator).validateEmailUnique(anyString());
        doNothing().when(userValidator).validatePasswordStrength(anyString());
        when(passwordUtil.encode(anyString())).thenReturn("hashed");
        when(userPersistenceMapper.toEntity(any())).thenReturn(entity);
        when(userRepository.save(any())).thenReturn(entity);
        when(userPersistenceMapper.toModel(entity)).thenReturn(
                UserModel.builder().id(entity.getId()).email("ana@test.com").role(Role.USER).build());

        var result = authService.register(model);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("ana@test.com");
        assertThat(result.getRole()).isEqualTo(Role.USER);
        verify(userRepository).save(any());
    }

    @Test
    void register_throwsConflict_whenEmailExists() {
        var model = UserModel.builder()
                .email("dup@test.com")
                .password("securePass1")
                .build();

        doThrow(new ConflictException("El email ya está registrado: dup@test.com"))
                .when(userValidator).validateEmailUnique("dup@test.com");

        assertThatThrownBy(() -> authService.register(model))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("dup@test.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsToken_whenCredentialsValid() {
        var entity = UserEntity.builder()
                .email("juan@test.com")
                .password("hashedPass")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(java.util.Optional.of(entity));
        when(passwordUtil.matches("rawPass", "hashedPass")).thenReturn(true);
        when(jwtUtil.generateToken("juan@test.com", "USER")).thenReturn("jwt-token");

        String token = authService.login("juan@test.com", "rawPass");
        assertThat(token).isEqualTo("jwt-token");
    }
}
