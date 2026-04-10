package com.sportlife.controller;

import com.sportlife.controller.dtos.response.OrderResponse;
import com.sportlife.controller.mappers.OrderControllerMapper;
import com.sportlife.core.services.OrderService;
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
 * Controller REST para el proceso de checkout (requiere JWT).
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints de órdenes de compra")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final OrderControllerMapper orderControllerMapper;

    @Operation(summary = "Procesar pago y crear orden desde el carrito")
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        log.info("POST /api/orders/checkout - userId={}", userId);
        var order = orderService.checkout(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderControllerMapper.toResponse(order));
    }
}
