package com.sportlife.persistence.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Objeto embebido que representa un ítem dentro de una orden.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEmbedded {

    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal subtotal;
}
