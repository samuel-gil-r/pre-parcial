package com.sportlife.persistence.mappers;

import com.sportlife.core.models.UserModel;
import com.sportlife.persistence.entities.UserEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre UserEntity (BD) y UserModel (dominio).
 */
@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserModel toModel(UserEntity entity);
    UserEntity toEntity(UserModel model);
}
