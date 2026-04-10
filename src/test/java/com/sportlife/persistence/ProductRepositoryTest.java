package com.sportlife.persistence;

import com.sportlife.core.models.ProductStatus;
import com.sportlife.persistence.entities.ProductEntity;
import com.sportlife.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Prueba de integración del repositorio de productos usando H2 en memoria.
 */
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productRepository.save(ProductEntity.builder()
                .name("Zapatillas Running Pro")
                .description("Alta amortiguación")
                .category("calzado")
                .price(BigDecimal.valueOf(120.00))
                .stock(15)
                .status(ProductStatus.ACTIVE)
                .build());

        productRepository.save(ProductEntity.builder()
                .name("Camiseta Dry-Fit")
                .description("Transpirable")
                .category("ropa")
                .price(BigDecimal.valueOf(35.00))
                .stock(50)
                .status(ProductStatus.ACTIVE)
                .build());

        productRepository.save(ProductEntity.builder()
                .name("Guantes Inactivos")
                .description("Descontinuado")
                .category("accesorios")
                .price(BigDecimal.valueOf(20.00))
                .stock(0)
                .status(ProductStatus.INACTIVE)
                .build());
    }

    @Test
    void findActiveWithFilters_returnsAllActive_whenNoFilters() {
        List<ProductEntity> results = productRepository.findActiveWithFilters(ProductStatus.ACTIVE, null, null);
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(p -> p.getStatus() == ProductStatus.ACTIVE);
    }

    @Test
    void findActiveWithFilters_filtersByCategory() {
        List<ProductEntity> results = productRepository.findActiveWithFilters(ProductStatus.ACTIVE, "calzado", null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Zapatillas Running Pro");
    }

    @Test
    void findActiveWithFilters_filtersByNameCaseInsensitive() {
        List<ProductEntity> results = productRepository.findActiveWithFilters(ProductStatus.ACTIVE, null, "zapatillas");
        assertThat(results).hasSize(1);
    }

    @Test
    void findActiveWithFilters_excludesInactive() {
        List<ProductEntity> results = productRepository.findActiveWithFilters(ProductStatus.ACTIVE, "accesorios", null);
        assertThat(results).isEmpty();
    }
}
