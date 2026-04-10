package com.sportlife.persistence.repositories;

import com.sportlife.persistence.entities.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio MongoDB para operaciones CRUD del carrito de compras.
 */
@Repository
public interface CartRepository extends MongoRepository<CartDocument, String> {
    Optional<CartDocument> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
