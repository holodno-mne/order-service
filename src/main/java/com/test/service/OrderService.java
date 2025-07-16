package com.test.service;

import com.test.client.ProductClient;
import com.test.dto.OrderDTO;
import com.test.entity.Order;
import com.test.entity.OrderItem;
import com.test.exception.BadRequestException;
import com.test.mapper.OrderMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @PersistenceContext
    EntityManager em;

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        order.setCreatedAt(orderDTO.getCreatedAt() != null ? orderDTO.getCreatedAt() : java.time.LocalDateTime.now());

        for (OrderItem item : order.getItems()) {

            var product = productClient.getProductById(item.getProductId());
            if (product == null) {
                throw new BadRequestException("Product not found");
            }
            if (item.getQuantity() > product.getQuantity()) {
                throw new BadRequestException("Not enough stock");
            }
            item.setPriceAtPurchase(product.getPrice());
            item.setOrder(order);
        }

        em.persist(order);
        return orderMapper.toDto(order);
    }


    public List<OrderDTO> getOrders() {
        List<Order> orders = em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = em.find(Order.class, id);
        return order != null ? orderMapper.toDto(order) : null;
    }

    @Transactional
    public boolean deleteOrderById(Long id) {
        Order order = em.find(Order.class, id);
        if (order != null) {
            em.remove(order);
            return true;
        }
        return false;
    }


}
