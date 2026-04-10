package com.sportlife.core.services;

import com.sportlife.core.models.CartModel;
import com.sportlife.core.utils.exceptions.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementación concreta del Strategy de pago con tarjeta de crédito.
 * En producción, se integraría con una pasarela real (Stripe, PayU, etc.).
 */
@Slf4j
@Service
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public String processPayment(CartModel cart) {
        log.info("Procesando pago con tarjeta para userId={}, total={}", cart.getUserId(), cart.getTotal());

        // Simulación de procesamiento. En producción: llamada a pasarela.
        boolean paymentApproved = simulateGateway();

        if (!paymentApproved) {
            log.warn("Pago rechazado para userId={}", cart.getUserId());
            throw new PaymentException("El pago fue rechazado por la pasarela");
        }

        String transactionId = UUID.randomUUID().toString();
        log.info("Pago aprobado. transactionId={}", transactionId);
        return transactionId;
    }

    private boolean simulateGateway() {
        return true; // Siempre aprueba en este entorno de demostración
    }
}
