package com.sportlife.controller;

import com.sportlife.controller.dtos.request.LoginRequest;
import com.sportlife.controller.dtos.request.RegisterRequest;
import com.sportlife.controller.dtos.response.LoginResponse;
import com.sportlife.controller.dtos.response.UserResponse;
import com.sportlife.controller.mappers.UserControllerMapper;
import com.sportlife.core.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para autenticación: registro y login de usuarios.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints de autenticación")
public class AuthController {

    private final AuthService authService;
    private final UserControllerMapper userControllerMapper;

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register - email={}", request.getEmail());
        var model = authService.register(userControllerMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userControllerMapper.toResponse(model));
    }

    @Operation(summary = "Login de usuario, devuelve JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - email={}", request.getEmail());
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
