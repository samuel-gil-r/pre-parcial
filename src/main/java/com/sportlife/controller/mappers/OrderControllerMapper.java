package com.sportlife.controller.mappers;

import com.sportlife.controller.dtos.response.OrderItemResponse;
import com.sportlife.controller.dtos.response.OrderResponse;
import com.sportlife.core.models.OrderItemModel;
import com.sportlife.core.models.OrderModel;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre OrderModel y OrderResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface OrderControllerMapper {
    OrderResponse toResponse(OrderModel model);
    OrderItemResponse toItemResponse(OrderItemModel model);
}
