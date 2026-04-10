package com.sportlife.controller;

import com.sportlife.controller.dtos.response.ProductResponse;
import com.sportlife.controller.mappers.ProductControllerMapper;
import com.sportlife.core.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller REST para el catálogo de productos.
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints del catálogo de productos")
public class ProductController {

    private final ProductService productService;
    private final ProductControllerMapper productControllerMapper;

    @Operation(summary = "Listar productos activos con filtros opcionales")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {
        log.info("GET /api/products - category={}, name={}", category, name);
        var products = productService.getActiveProducts(category, name)
                .stream()
                .map(productControllerMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Ver detalle de un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        log.info("GET /api/products/{}", id);
        return ResponseEntity.ok(productControllerMapper.toResponse(productService.getProductById(id)));
    }
}
