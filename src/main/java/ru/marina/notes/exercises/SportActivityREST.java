package ru.marina.notes.exercises;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("/sport-activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SportActivityREST {

    @POST
    String addSportActivity(AddSportActivityRequest request);

    class AddSportActivityRequest {
        public SportActivity sportActivity;
        public List<Exercise> exercises;
    }

    @GET
    @Path("{activityId}")
    SportActivity getSportActivity(@PathParam("activityId") String activityId);

    @PUT
    void updateSportActivity(SportActivity sportActivity);

    @GET
    @Path("search")
    List<SportActivity> getActivitiesOfDay(@QueryParam("date") LocalDate date);

    @GET
    @Path("{activityId}/exercises")
    List<Exercise> getExercises(@PathParam("activityId") String activityId);

    @DELETE
    @Path("{activityId}")
    void deleteSportActivity(@PathParam("activityId") String activityId);
}
