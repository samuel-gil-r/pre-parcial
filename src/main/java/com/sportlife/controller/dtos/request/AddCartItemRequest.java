package com.sportlife.controller.dtos.request;

import lombok.Data;

import java.util.UUID;

/**
 * DTO de entrada para agregar un producto al carrito.
 */
@Data
public class AddCartItemRequest {
    private UUID productId;
    private int quantity;
}
