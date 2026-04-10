package com.sportlife.persistence.mappers;

import com.sportlife.core.models.OrderItemModel;
import com.sportlife.core.models.OrderModel;
import com.sportlife.persistence.entities.OrderEntity;
import com.sportlife.persistence.entities.OrderItemEmbedded;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper que convierte entre OrderEntity (BD) y OrderModel (dominio).
 */
@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {
    OrderModel toModel(OrderEntity entity);
    OrderEntity toEntity(OrderModel model);
    OrderItemModel toItemModel(OrderItemEmbedded embedded);
    OrderItemEmbedded toItemEmbedded(OrderItemModel model);
}
