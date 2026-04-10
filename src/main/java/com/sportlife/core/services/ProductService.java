package com.sportlife.core.services;

import com.sportlife.core.models.ProductModel;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz del servicio de productos.
 */
public interface ProductService {
    List<ProductModel> getActiveProducts(String category, String name);
    ProductModel getProductById(UUID id);
}
