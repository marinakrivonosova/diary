package ru.marina.notes.healthparams;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("/health-parameters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface HealthParametersREST {

    @POST
    String addHealthParameters(HealthParameters healthParameters);

    @GET
    @Path("{healthParameterId}")
    HealthParameters getHealthParameters(@PathParam("healthParameterId") String healthParameterId);

    @PUT
    void updateHealthParameters(HealthParameters healthParameters);

    @GET
    @Path("search")
    List<HealthParameters> getHealthParamsOfDay(@QueryParam("date") LocalDate date);

    @DELETE
    @Path("{healthParameterId}")
    void deleteHealthParameters(@PathParam("healthParameterId") String healthParameterId);
}

