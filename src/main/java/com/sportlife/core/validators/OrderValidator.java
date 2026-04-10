package com.sportlife.core.validators;

import com.sportlife.core.models.CartModel;
import com.sportlife.core.utils.exceptions.ValidationException;
import com.sportlife.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validaciones de negocio para órdenes: carrito no vacío y stock al momento del checkout.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final ProductRepository productRepository;

    public void validateCartForCheckout(CartModel cart) {
        log.debug("Validando carrito para checkout, userId={}", cart.getUserId());
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ValidationException("No se puede procesar un pago con el carrito vacío");
        }

        cart.getItems().forEach(item -> {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ValidationException(
                            "Producto del carrito ya no existe: " + item.getProductId()));

            if (item.getQuantity() > product.getStock()) {
                throw new ValidationException(
                        String.format("Stock insuficiente para '%s' al momento del pago. Disponible: %d",
                                item.getProductName(), product.getStock()));
            }
        });
    }
}
