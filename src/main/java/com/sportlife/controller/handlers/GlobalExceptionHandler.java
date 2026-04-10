package com.sportlife.controller.handlers;

import com.sportlife.core.utils.exceptions.ConflictException;
import com.sportlife.core.utils.exceptions.PaymentException;
import com.sportlife.core.utils.exceptions.ResourceNotFoundException;
import com.sportlife.core.utils.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones que normaliza las respuestas de error.
 * Respuesta estándar: { "status": 4xx, "message": "...", "timestamp": "..." }
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("ResourceNotFoundException: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex) {
        log.warn("ConflictException: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        log.warn("ValidationException: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, Object>> handlePayment(PaymentException ex) {
        log.warn("PaymentException: {}", ex.getMessage());
        return buildResponse(HttpStatus.PAYMENT_REQUIRED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleBeanValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        log.warn("Bean validation failed: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(status).body(body);
    }
}
