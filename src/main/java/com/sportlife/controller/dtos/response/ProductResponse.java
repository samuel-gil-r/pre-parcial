package com.sportlife.controller.dtos.response;

import com.sportlife.core.models.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de salida con la información pública de un producto.
 */
@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private List<String> images;
    private ProductStatus status;
    private LocalDateTime createdAt;
}
