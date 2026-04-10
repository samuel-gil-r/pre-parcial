package com.sportlife.controller.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de salida con el resumen completo del carrito del usuario.
 */
@Data
public class CartResponse {
    private String id;
    private UUID userId;
    private List<CartItemResponse> items;
    private BigDecimal total;
    private LocalDateTime updatedAt;
}
