package com.sportlife.persistence.mappers;

import com.sportlife.core.models.ProductModel;
import com.sportlife.persistence.entities.ProductEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre ProductEntity (BD) y ProductModel (dominio).
 */
@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    ProductModel toModel(ProductEntity entity);
    ProductEntity toEntity(ProductModel model);
}
