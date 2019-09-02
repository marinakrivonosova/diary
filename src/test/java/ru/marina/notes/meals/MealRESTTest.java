package ru.marina.notes.meals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.marina.notes.MyParamConverterProvider;
import ru.marina.notes.PersonalConfiguration;

import javax.ws.rs.core.Application;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MealRESTTest extends JerseyTest {
    private final Meal meal = new Meal("mealId", "title", "text", LocalDate.of(2019, 12, 12), Timestamp.from(Instant.now()));

    private final FoodInfo foodInfo1 = new FoodInfo("foodInfo1", "mealId", "soup", 200, 380);
    private final FoodInfo foodInfo2 = new FoodInfo("foodInfo2", "mealId", "sandwich", 150, 400);

    private MealDao mealDao;
    private FoodInfoDao foodInfoDao;
    private AddMealTransaction addMealTransaction;
    private PersonalConfiguration personalConfiguration;

    public MealsREST createClient() {
        return WebResourceFactory.newResource(MealsREST.class, target());
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        mealDao = mock(MealDao.class);
        foodInfoDao = mock(FoodInfoDao.class);
        addMealTransaction = mock(AddMealTransaction.class);
        personalConfiguration = mock(PersonalConfiguration.class);

        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        return new ResourceConfig()
                .register(new MealsRESTImplementation(mealDao, foodInfoDao, addMealTransaction, personalConfiguration))
                .register(MyParamConverterProvider.class)
                .register(jacksonJaxbJsonProvider);
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        config.register(jacksonJaxbJsonProvider);
    }

    @Test
    public void testAddMeal() {
        final MealsREST.AddMealRequest addMealRequest = new MealsREST.AddMealRequest();
        addMealRequest.foodInfo = Arrays.asList(foodInfo1, foodInfo2);
        addMealRequest.meal = meal;

        final String mealId = createClient().addMeal(addMealRequest);

        verify(addMealTransaction).insertMeal(meal, Arrays.asList(foodInfo1, foodInfo2));

        assertEquals("mealId", mealId);
    }

    @Test
    public void testGetMeal() {
        when(mealDao.getMeal(any())).thenReturn(meal);

        final Meal resultMeal = createClient().getMeal(meal.getNoteId());

        verify(mealDao).getMeal(meal.getNoteId());

        assertEquals(meal, resultMeal);
    }

    @Test
    public void testUpdateMeal() {
        createClient().updateMeal(meal);

        verify(mealDao).updateMeal(meal);
    }

    @Test
    public void testGetMealsOfDay() {
        when(mealDao.getMealsOfDay(any())).thenReturn(Collections.singletonList(meal));

        final List<Meal> meals = createClient().getMealsOfDay(LocalDate.of(2019, 12, 12));

        verify(mealDao).getMealsOfDay(LocalDate.of(2019, 12, 12));
        assertEquals(Collections.singletonList(meal), meals);
    }

    @Test
    public void testGetFoodInfoTest() {
        when(foodInfoDao.listFoodInfo(any())).thenReturn(Arrays.asList(foodInfo1, foodInfo2));

        final List<FoodInfo> foodInfoList = createClient().getFoodInfo(meal.getNoteId());
        verify(foodInfoDao).listFoodInfo(meal.getNoteId());

        assertEquals(Arrays.asList(foodInfo1, foodInfo2), foodInfoList);
    }

    @Test
    public void testCountCalories() {
        when(foodInfoDao.caloriesOfDay(any())).thenReturn(780);
        when(personalConfiguration.getDailyCaloriesNorm()).thenReturn(2000);

        final MealsREST.CaloriesForDay caloriesForDay = createClient().countCaloriesLeftForToday(LocalDate.of(2019, 12, 12));

        verify(foodInfoDao).caloriesOfDay(LocalDate.of(2019, 12, 12));

        assertNotNull(caloriesForDay);
        assertEquals(780, caloriesForDay.caloriesEaten);
        assertEquals(2000, caloriesForDay.dailyCaloriesAmount);
    }

    @Test
    public void testDeleteMeal() {
        createClient().deleteMeal(meal.getNoteId());

        verify(mealDao).deleteMeal(meal.getNoteId());
    }

}
