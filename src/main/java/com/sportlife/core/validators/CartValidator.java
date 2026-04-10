package com.sportlife.core.validators;

import com.sportlife.core.models.CartModel;
import com.sportlife.core.utils.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validaciones de negocio para el carrito: stock disponible.
 */
@Slf4j
@Component
public class CartValidator {

    public void validateStock(int requestedQuantity, int availableStock, String productName) {
        log.debug("Validando stock para '{}': solicitado={}, disponible={}", productName, requestedQuantity, availableStock);
        if (requestedQuantity > availableStock) {
            throw new ValidationException(
                    String.format("Stock insuficiente para '%s'. Disponible: %d, Solicitado: %d",
                            productName, availableStock, requestedQuantity));
        }
    }

    public void validateNotEmpty(CartModel cart) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ValidationException("El carrito está vacío");
        }
    }
}
