package com.sportlife.persistence.entities;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Documento embebido que representa un ítem dentro del carrito (MongoDB).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDocument {

    private UUID productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
