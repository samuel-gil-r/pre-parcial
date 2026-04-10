package com.sportlife.core.utils.exceptions;

/**
 * Lanzada cuando existe un conflicto de estado (HTTP 409), p.ej. email duplicado.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
