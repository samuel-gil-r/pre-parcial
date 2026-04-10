package com.sportlife.controller.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de salida para un ítem del carrito.
 */
@Data
public class CartItemResponse {
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
