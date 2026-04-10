package com.sportlife.controller;

import com.sportlife.controller.dtos.request.AddCartItemRequest;
import com.sportlife.controller.dtos.response.CartResponse;
import com.sportlife.controller.mappers.CartControllerMapper;
import com.sportlife.core.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller REST para gestión del carrito de compras (requiere JWT).
 */
@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints del carrito de compras")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;
    private final CartControllerMapper cartControllerMapper;

    @Operation(summary = "Agregar producto al carrito")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestBody AddCartItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        log.info("POST /api/cart/items - userId={}, productId={}", userId, request.getProductId());
        var cart = cartService.addItem(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartControllerMapper.toResponse(cart));
    }

    @Operation(summary = "Ver resumen del carrito")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        log.info("GET /api/cart - userId={}", userId);
        return ResponseEntity.ok(cartControllerMapper.toResponse(cartService.getCart(userId)));
    }
}
