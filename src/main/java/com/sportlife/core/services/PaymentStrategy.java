package com.sportlife.core.services;

import com.sportlife.core.models.CartModel;

/**
 * Interfaz Strategy para diferentes métodos de pago.
 * Permite intercambiar implementaciones sin modificar el servicio de órdenes.
 */
public interface PaymentStrategy {
    /**
     * Procesa el pago del carrito y retorna el ID de transacción.
     */
    String processPayment(CartModel cart);
}
