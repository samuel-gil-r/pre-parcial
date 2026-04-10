package com.sportlife.core.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio de producto usado internamente por los servicios.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel {
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
