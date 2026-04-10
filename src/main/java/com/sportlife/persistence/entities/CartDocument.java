package com.sportlife.persistence.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Documento MongoDB que representa el carrito de compras de un usuario.
 */
@Document(collection = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private UUID userId;

    private List<CartItemDocument> items;

    private BigDecimal total;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
