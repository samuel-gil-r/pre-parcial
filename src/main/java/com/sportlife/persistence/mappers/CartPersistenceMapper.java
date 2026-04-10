package com.sportlife.persistence.mappers;

import com.sportlife.core.models.CartItemModel;
import com.sportlife.core.models.CartModel;
import com.sportlife.persistence.entities.CartDocument;
import com.sportlife.persistence.entities.CartItemDocument;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre CartDocument (MongoDB) y CartModel (dominio).
 */
@Mapper(componentModel = "spring")
public interface CartPersistenceMapper {
    CartModel toModel(CartDocument document);
    CartDocument toDocument(CartModel model);
    CartItemModel toItemModel(CartItemDocument document);
    CartItemDocument toItemDocument(CartItemModel model);
}
