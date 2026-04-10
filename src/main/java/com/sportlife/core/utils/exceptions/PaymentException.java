package com.sportlife.core.utils.exceptions;

/**
 * Lanzada cuando el procesamiento de pago falla (HTTP 402).
 */
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
