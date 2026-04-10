package com.sportlife.core.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio del carrito de compras.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartModel {
    private String id;
    private UUID userId;
    private List<CartItemModel> items;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
