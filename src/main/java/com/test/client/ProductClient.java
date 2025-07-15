package com.test.client;

import com.test.dto.ProductDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient(configKey = "inventory-api")
public interface ProductClient {

    @GET
    @Path("{id}")
    ProductDTO getProductById(@PathParam("id") Long id);
}
