package com.sportlife.core.validators;

import com.sportlife.core.models.ProductModel;
import com.sportlife.core.models.ProductStatus;
import com.sportlife.core.utils.exceptions.ResourceNotFoundException;
import com.sportlife.core.utils.exceptions.ValidationException;
import com.sportlife.persistence.mappers.ProductPersistenceMapper;
import com.sportlife.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validaciones de negocio para productos: existencia y estado activo.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public ProductModel validateExistsAndActive(UUID productId) {
        log.debug("Validando producto activo: {}", productId);
        var entity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productId));

        if (entity.getStatus() != ProductStatus.ACTIVE) {
            throw new ValidationException("El producto no está disponible: " + productId);
        }
        return productPersistenceMapper.toModel(entity);
    }

    public ProductModel validateExists(UUID productId) {
        log.debug("Validando existencia de producto: {}", productId);
        return productRepository.findById(productId)
                .map(productPersistenceMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productId));
    }
}
