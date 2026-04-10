package com.sportlife.core.services;

import com.sportlife.core.models.CartItemModel;
import com.sportlife.core.models.CartModel;
import com.sportlife.core.validators.CartValidator;
import com.sportlife.core.validators.ProductValidator;
import com.sportlife.persistence.entities.CartDocument;
import com.sportlife.persistence.mappers.CartPersistenceMapper;
import com.sportlife.persistence.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementación del servicio del carrito usando MongoDB.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartPersistenceMapper cartPersistenceMapper;
    private final ProductValidator productValidator;
    private final CartValidator cartValidator;

    @Override
    public CartModel addItem(UUID userId, UUID productId, int quantity) {
        log.info("Agregando al carrito. userId={}, productId={}, qty={}", userId, productId, quantity);

        cartValidator.validateAddItemRequest(productId, quantity);
        var product = productValidator.validateExistsAndActive(productId);
        cartValidator.validateStock(quantity, product.getStock(), product.getName());

        var cartDoc = cartRepository.findByUserId(userId)
                .orElseGet(() -> CartDocument.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .total(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .build());

        var cart = cartPersistenceMapper.toModel(cartDoc);
        if (cart.getItems() == null) cart.setItems(new ArrayList<>());

        // Si el producto ya está en el carrito, actualiza la cantidad
        var existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            var item = existing.get();
            int newQty = item.getQuantity() + quantity;
            cartValidator.validateStock(newQty, product.getStock(), product.getName());
            item.setQuantity(newQty);
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(newQty)));
        } else {
            var newItem = CartItemModel.builder()
                    .productId(productId)
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(quantity)
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();
            cart.getItems().add(newItem);
        }

        // Recalcular total
        BigDecimal total = cart.getItems().stream()
                .map(CartItemModel::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
        cart.setUpdatedAt(LocalDateTime.now());

        var savedDoc = cartRepository.save(cartPersistenceMapper.toDocument(cart));
        log.debug("Carrito actualizado. total={}", total);
        return cartPersistenceMapper.toModel(savedDoc);
    }

    @Override
    public CartModel getCart(UUID userId) {
        log.info("Obteniendo carrito para userId={}", userId);
        var doc = cartRepository.findByUserId(userId)
                .orElseGet(() -> CartDocument.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .total(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
        return cartPersistenceMapper.toModel(doc);
    }
}
