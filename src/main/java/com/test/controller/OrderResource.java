package com.test.controller;


import com.test.dto.OrderDTO;
import com.test.service.OrderService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    OrderService orderService;

    @GET
    public List<OrderDTO> getOrders() {
        log.info("Request a list of all orders");
        return orderService.getOrders();
    }

    @POST
    public Response createOrder(OrderDTO orderDTO) {
        log.info("Create order");
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return Response.status(Response.Status.CREATED)
                .entity(createdOrder)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        log.info("Get order by id: {}", id);
        OrderDTO order = orderService.getOrderById(id);
        if (order == null) {
            log.warn("No order found with id: {}", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found")
                    .build();
        }
        return Response.ok(order).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteOrderById(@PathParam("id") Long id) {
        log.info("Delete order by id: {}", id);
        boolean deleted = orderService.deleteOrderById(id);
        if (!deleted) {
            log.warn("No order found to be deleted with id: {}", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found")
                    .build();
        }
        log.info("Successfully deleted order by id: {}", id);
        return Response.noContent().build();
    }
}
