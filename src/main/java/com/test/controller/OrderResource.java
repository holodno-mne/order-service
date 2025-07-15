package com.test.controller;


import com.test.client.ProductClient;
import com.test.dto.OrderDTO;
import com.test.dto.OrderItemDTO;
import com.test.dto.ProductDTO;
import com.test.entity.Order;
import com.test.entity.OrderItem;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    EntityManager em;

    @Inject
    @RestClient
    ProductClient productClient;

    @GET
    public List<Order> getOrders() {
        log.info("Request a list of all orders");
        return em.createQuery("FROM Order", Order.class).getResultList();
    }

    @POST
    @Transactional
    public Response createOrder(List<OrderItemDTO> items) {
        log.info("Create order");
        Order order = new Order();

        for (OrderItemDTO dto : items) {
            try {
                ProductDTO product = productClient.getProductById(dto.productId);
                if (product == null) {
                    log.warn("Product not found");
                    throw new BadRequestException("Product with id " + dto.productId + " not found");
                }
                if (product.quantity < dto.quantity) {
                    log.warn("Incorrect quantity");
                    throw new BadRequestException("Not enough stock for product id " + dto.productId);
                }
                OrderItem item = new OrderItem();
                item.setProductId(dto.productId);
                item.setQuantity(dto.quantity);
                item.setPriceAtPurchase(product.price);

                item.setOrder(order);
                order.getItems().add(item);

                log.info("Order is created");
            } catch (Exception e) {
                throw new BadRequestException("Failed to create order with product id " + dto.productId);
            }

        }
        em.persist(order);
        return Response.status(201).entity(new OrderDTO(order)).build();
    }

    @GET
    @Path("/{id}")
    public Order getOrderById(@PathParam("id") Long id) {
        return em.find(Order.class, id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteOrderById(@PathParam("id") Long id) {
        Order order = em.find(Order.class, id);
        if (order != null) {
            em.remove(order);
            log.info("Order is deleted");
        } else {
            log.warn("Incorrect order id");
            throw new NotFoundException("Order with id " + id + " not found");
        }
    }
}
