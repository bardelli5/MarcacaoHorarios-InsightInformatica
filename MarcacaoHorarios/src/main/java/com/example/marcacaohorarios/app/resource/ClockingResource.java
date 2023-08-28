package com.example.marcacaohorarios.app.resource;

import com.example.marcacaohorarios.app.dto.request.ClockingRequest;
import com.example.marcacaohorarios.app.dto.response.ErrorResponse;
import com.example.marcacaohorarios.app.service.ClockingService;
import com.example.marcacaohorarios.domain.exception.InvalidClockingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clocking")
public class ClockingResource {

    ClockingService service;

    @Inject
    public ClockingResource(ClockingService service) {
        this.service = service;
    }

    @POST
    @Produces({"application/json", MediaType.APPLICATION_JSON})
    public Response createClocking(ClockingRequest request) {
        try {
            return Response.ok(service.newClocking(request)).build();
        } catch (InvalidClockingException e)  {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}
