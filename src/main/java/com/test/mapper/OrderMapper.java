package com.test.mapper;

import com.test.dto.OrderDTO;
import com.test.dto.OrderItemDTO;
import com.test.entity.Order;
import com.test.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface OrderMapper {

    OrderDTO toDto(Order order);

    OrderItemDTO toDto(OrderItem orderItem);

    Order toEntity(OrderDTO orderDTO);

    @Mapping(target = "priceAtPurchase", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
