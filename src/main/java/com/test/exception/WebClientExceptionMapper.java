package com.test.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebClientExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();

        if (status == 404) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Product not found", "Product doesnt exist in inventory"))
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("External call error", exception.getMessage()))
                .build();
    }
}
