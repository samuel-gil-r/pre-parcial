package com.sportlife.controller.dtos.response;

import com.sportlife.core.models.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de salida con los datos completos de una orden de compra.
 */
@Data
public class OrderResponse {
    private UUID id;
    private UUID userId;
    private List<OrderItemResponse> items;
    private BigDecimal total;
    private OrderStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
}
