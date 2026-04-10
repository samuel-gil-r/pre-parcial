package com.sportlife.controller.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de salida para un ítem de una orden.
 */
@Data
public class OrderItemResponse {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal subtotal;
}
