package com.sportlife.core.utils.exceptions;

/**
 * Lanzada cuando un recurso no se encuentra en el sistema (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
