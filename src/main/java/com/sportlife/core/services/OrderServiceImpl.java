package com.sportlife.core.services;

import com.sportlife.core.models.CartModel;
import com.sportlife.core.models.OrderItemModel;
import com.sportlife.core.models.OrderModel;
import com.sportlife.core.models.OrderStatus;
import com.sportlife.core.utils.exceptions.PaymentException;
import com.sportlife.core.validators.CartValidator;
import com.sportlife.core.validators.OrderValidator;
import com.sportlife.persistence.mappers.OrderPersistenceMapper;
import com.sportlife.persistence.repositories.CartRepository;
import com.sportlife.persistence.repositories.OrderRepository;
import com.sportlife.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de checkout: valida, procesa pago, crea orden y descuenta stock.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;
    private final ProductRepository productRepository;
    private final CartValidator cartValidator;
    private final OrderValidator orderValidator;
    private final PaymentStrategy paymentStrategy;

    @Override
    @Transactional
    public OrderModel checkout(UUID userId) {
        log.info("Iniciando checkout para userId={}", userId);

        CartModel cart = cartService.getCart(userId);
        cartValidator.validateNotEmpty(cart);
        orderValidator.validateCartForCheckout(cart);

        String transactionId;
        OrderStatus status;
        try {
            transactionId = paymentStrategy.processPayment(cart);
            status = OrderStatus.PAID;

            // Descontar stock
            cart.getItems().forEach(item ->
                    productRepository.findById(item.getProductId()).ifPresent(p -> {
                        p.setStock(p.getStock() - item.getQuantity());
                        productRepository.save(p);
                    }));

            // Vaciar carrito
            cartRepository.deleteByUserId(userId);

        } catch (PaymentException e) {
            log.error("Pago rechazado para userId={}: {}", userId, e.getMessage());
            status = OrderStatus.REJECTED;
            transactionId = null;
        }

        var items = cart.getItems().stream()
                .map(ci -> OrderItemModel.builder()
                        .productId(ci.getProductId())
                        .productName(ci.getProductName())
                        .quantity(ci.getQuantity())
                        .subtotal(ci.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        var orderModel = OrderModel.builder()
                .userId(userId)
                .items(items)
                .total(cart.getTotal())
                .status(status)
                .transactionId(transactionId)
                .build();

        var saved = orderRepository.save(orderPersistenceMapper.toEntity(orderModel));
        log.info("Orden creada id={} status={}", saved.getId(), status);
        return orderPersistenceMapper.toModel(saved);
    }
}
