package com.example.marcacaohorarios.app.resource;

import com.example.marcacaohorarios.app.dto.request.WorkHourRequest;
import com.example.marcacaohorarios.app.dto.response.ErrorResponse;
import com.example.marcacaohorarios.app.service.WorkHourService;
import com.example.marcacaohorarios.domain.exception.InvalidWorkHourRequest;
import com.example.marcacaohorarios.domain.exception.WorkHourOversizeException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/work-hour")
public class WorkHourResource {

    WorkHourService workHourService;

    @Inject
    public WorkHourResource(WorkHourService workHourService) {
        this.workHourService = workHourService;
    }

    @POST
    @Produces({"application/json", MediaType.APPLICATION_JSON})
    public Response createWorkHour(WorkHourRequest request) {
        try {
            return Response.ok(workHourService.newWorkHour(request)).build();
        } catch (WorkHourOversizeException | InvalidWorkHourRequest e) {
            return Response.status(400).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Produces({"application/json", MediaType.APPLICATION_JSON})
    public Response deleteWorkHour(WorkHourRequest request) {
        return Response.ok(workHourService.deleteWorkHour(request)).build();
    }
}
