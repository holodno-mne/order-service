package com.test.dto;

import com.test.entity.Order;
import com.test.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    public LocalDateTime createdAt;
    public List<OrderItemDTO> items;

    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.createdAt = order.getCreatedAt();
        this.items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            this.items.add(new OrderItemDTO(item));
        }
    }
}
