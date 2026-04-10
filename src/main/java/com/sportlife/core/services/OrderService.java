package com.sportlife.core.services;

import com.sportlife.core.models.OrderModel;

import java.util.UUID;

/**
 * Interfaz del servicio de órdenes de compra.
 */
public interface OrderService {
    OrderModel checkout(UUID userId);
}
