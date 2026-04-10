package com.sportlife.core.utils.exceptions;

/**
 * Lanzada cuando falla una regla de negocio personalizada (HTTP 400).
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
