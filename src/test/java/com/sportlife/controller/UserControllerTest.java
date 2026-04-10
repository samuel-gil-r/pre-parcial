package com.sportlife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportlife.controller.dtos.request.LoginRequest;
import com.sportlife.controller.dtos.request.RegisterRequest;
import com.sportlife.controller.mappers.UserControllerMapper;
import com.sportlife.core.models.Role;
import com.sportlife.core.models.UserModel;
import com.sportlife.core.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba del AuthController con MockMvc: verifica rutas, status y estructura de respuesta.
 */
@WebMvcTest(AuthController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserControllerMapper userControllerMapper;

    @MockBean
    private com.sportlife.config.JwtFilter jwtFilter;

    @Test
    void register_returnsCreated_whenValidRequest() throws Exception {
        var request = new RegisterRequest();
        request.setName("Juan Pérez");
        request.setEmail("juan@test.com");
        request.setPassword("password123");

        var userModel = UserModel.builder()
                .id(UUID.randomUUID())
                .name("Juan Pérez")
                .email("juan@test.com")
                .role(Role.USER)
                .build();

        when(userControllerMapper.toModel(any(RegisterRequest.class))).thenReturn(userModel);
        when(authService.register(any(UserModel.class))).thenReturn(userModel);
        when(userControllerMapper.toResponse(any(UserModel.class))).thenReturn(new com.sportlife.controller.dtos.response.UserResponse());

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void register_returnsBadRequest_whenEmailInvalid() throws Exception {
        var request = new RegisterRequest();
        request.setName("Test");
        request.setEmail("not-an-email");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenPasswordTooShort() throws Exception {
        var request = new RegisterRequest();
        request.setName("Test");
        request.setEmail("test@test.com");
        request.setPassword("123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returnsOk_whenValidCredentials() throws Exception {
        var request = new LoginRequest();
        request.setEmail("juan@test.com");
        request.setPassword("password123");

        when(authService.login("juan@test.com", "password123")).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }
}
