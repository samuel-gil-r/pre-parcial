package com.sportlife.core.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio de orden de compra.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private UUID id;
    private UUID userId;
    private List<OrderItemModel> items;
    private BigDecimal total;
    private OrderStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
}
