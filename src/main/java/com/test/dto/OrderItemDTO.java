package com.test.dto;

import com.test.entity.OrderItem;

public class OrderItemDTO {
    public Long productId;
    public int quantity;
    public double priceAtPurchase;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem item) {
        this.productId = item.getProductId();
        this.quantity = item.getQuantity();
        this.priceAtPurchase = item.getPriceAtPurchase();
    }
}
