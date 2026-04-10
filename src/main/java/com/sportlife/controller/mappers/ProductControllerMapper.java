package com.sportlife.controller.mappers;

import com.sportlife.controller.dtos.response.ProductResponse;
import com.sportlife.core.models.ProductModel;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre ProductModel y ProductResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface ProductControllerMapper {
    ProductResponse toResponse(ProductModel model);
}
