package com.sportlife.core.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Modelo de dominio de ítem del carrito.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemModel {
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
