package com.test.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    public Long id;
    public LocalDateTime createdAt;
    public List<OrderItemDTO> items;
}
