package com.sportlife.core.services;

import com.sportlife.core.models.ProductModel;
import com.sportlife.core.models.ProductStatus;
import com.sportlife.core.validators.ProductValidator;
import com.sportlife.persistence.mappers.ProductPersistenceMapper;
import com.sportlife.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de catálogo de productos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final ProductValidator productValidator;

    @Override
    public List<ProductModel> getActiveProducts(String category, String name) {
        log.info("Listando productos activos. category={}, name={}", category, name);
        var entities = productRepository.findActiveWithFilters(ProductStatus.ACTIVE, category, name);
        log.debug("Encontrados {} productos", entities.size());
        return entities.stream()
                .map(productPersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductModel getProductById(UUID id) {
        log.info("Obteniendo producto por id: {}", id);
        return productValidator.validateExists(id);
    }
}
