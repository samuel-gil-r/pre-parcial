package com.sportlife.controller.mappers;

import com.sportlife.controller.dtos.response.CartItemResponse;
import com.sportlife.controller.dtos.response.CartResponse;
import com.sportlife.core.models.CartItemModel;
import com.sportlife.core.models.CartModel;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre CartModel y CartResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface CartControllerMapper {
    CartResponse toResponse(CartModel model);
    CartItemResponse toItemResponse(CartItemModel model);
}
