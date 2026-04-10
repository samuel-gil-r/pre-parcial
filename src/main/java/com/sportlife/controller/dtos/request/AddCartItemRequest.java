package com.sportlife.controller.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO de entrada para agregar un producto al carrito.
 */
@Data
public class AddCartItemRequest {

    @NotNull(message = "El productId es obligatorio")
    private UUID productId;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int quantity;
}
