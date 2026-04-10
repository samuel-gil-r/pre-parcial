package com.sportlife.persistence.repositories;

import com.sportlife.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para operaciones CRUD de usuarios en PostgreSQL.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
