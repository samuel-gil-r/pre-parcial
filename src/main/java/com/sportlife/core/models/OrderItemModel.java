package com.sportlife.core.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Modelo de dominio de ítem de una orden.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemModel {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal subtotal;
}
