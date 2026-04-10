package com.sportlife.controller.mappers;

import com.sportlife.controller.dtos.request.RegisterRequest;
import com.sportlife.controller.dtos.response.UserResponse;
import com.sportlife.core.models.UserModel;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre DTOs de usuario y UserModel.
 */
@Mapper(componentModel = "spring")
public interface UserControllerMapper {
    UserModel toModel(RegisterRequest request);
    UserResponse toResponse(UserModel model);
}
