package com.sportlife.core.services;

import com.sportlife.core.models.CartModel;

import java.util.UUID;

/**
 * Interfaz del servicio del carrito de compras.
 */
public interface CartService {
    CartModel addItem(UUID userId, UUID productId, int quantity);
    CartModel getCart(UUID userId);
}
