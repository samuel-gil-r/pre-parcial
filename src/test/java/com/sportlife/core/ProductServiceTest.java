package com.sportlife.core;

import com.sportlife.core.models.ProductModel;
import com.sportlife.core.models.ProductStatus;
import com.sportlife.core.services.ProductServiceImpl;
import com.sportlife.core.validators.ProductValidator;
import com.sportlife.persistence.entities.ProductEntity;
import com.sportlife.persistence.mappers.ProductPersistenceMapper;
import com.sportlife.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de productos con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductPersistenceMapper productPersistenceMapper;
    @Mock private ProductValidator productValidator;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getActiveProducts_returnsFilteredList() {
        var entity = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Camiseta Deportiva")
                .category("ropa")
                .price(BigDecimal.valueOf(49.99))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        var model = ProductModel.builder()
                .id(entity.getId())
                .name("Camiseta Deportiva")
                .category("ropa")
                .price(BigDecimal.valueOf(49.99))
                .status(ProductStatus.ACTIVE)
                .build();

        when(productRepository.findActiveWithFilters(eq(ProductStatus.ACTIVE), eq("ropa"), isNull()))
                .thenReturn(List.of(entity));
        when(productPersistenceMapper.toModel(entity)).thenReturn(model);

        var result = productService.getActiveProducts("ropa", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Camiseta Deportiva");
    }

    @Test
    void getActiveProducts_returnsEmpty_whenNoMatch() {
        when(productRepository.findActiveWithFilters(any(), any(), any())).thenReturn(List.of());
        var result = productService.getActiveProducts("inexistente", null);
        assertThat(result).isEmpty();
    }

    @Test
    void getProductById_returnsProduct_whenExists() {
        UUID id = UUID.randomUUID();
        var model = ProductModel.builder().id(id).name("Balón").build();
        when(productValidator.validateExists(id)).thenReturn(model);
        var result = productService.getProductById(id);
        assertThat(result.getId()).isEqualTo(id);
    }
}
