package ru.marina.notes.meals;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("/meals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MealsREST {

    @POST
    String addMeal(AddMealRequest request);

    class AddMealRequest {
        public Meal meal;
        public List<FoodInfo> foodInfo;
    }

    @GET
    @Path("{mealId}")
    Meal getMeal(@PathParam("mealId") String mealId);

    @PUT
    void updateMeal(Meal meal);

    @GET
    @Path("search")
    List<Meal> getMealsOfDay(@QueryParam("date") LocalDate date);

    @GET
    @Path("count-calories")
    CaloriesForDay countCaloriesLeftForToday(@QueryParam("date") LocalDate date);

    class CaloriesForDay {
        public int dailyCaloriesAmount;
        public int caloriesEaten;
    }

    @GET
    @Path("{mealId}/food-info")
    List<FoodInfo> getFoodInfo(@PathParam("mealId") String mealId);

    @DELETE
    @Path("{mealId}")
    void deleteMeal(@PathParam("mealId") String mealId);
}
