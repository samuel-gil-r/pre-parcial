package com.sportlife.persistence.entities;

import com.sportlife.core.models.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA de orden almacenada en PostgreSQL.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItemEmbedded> items;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String transactionId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
