package com.sportlife.persistence.repositories;

import com.sportlife.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para operaciones CRUD de órdenes en PostgreSQL.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByUserId(UUID userId);
}
