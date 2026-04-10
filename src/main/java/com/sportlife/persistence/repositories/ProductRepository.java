package com.sportlife.persistence.repositories;

import com.sportlife.core.models.ProductStatus;
import com.sportlife.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para operaciones CRUD de productos en PostgreSQL.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("SELECT p FROM ProductEntity p WHERE p.status = :status " +
           "AND (:category IS NULL OR p.category = :category) " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<ProductEntity> findActiveWithFilters(
            @Param("status") ProductStatus status,
            @Param("category") String category,
            @Param("name") String name
    );
}
