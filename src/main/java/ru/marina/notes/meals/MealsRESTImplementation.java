package ru.marina.notes.meals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.marina.notes.PersonalConfiguration;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

public class MealsRESTImplementation implements MealsREST {
    private static final Logger logger = LoggerFactory.getLogger(MealsRESTImplementation.class);

    private final MealDao mealsDao;
    private final FoodInfoDao foodInfoDao;
    private final AddMealTransaction addMealTransaction;
    private final PersonalConfiguration personalConfiguration;

    @Inject
    public MealsRESTImplementation(final MealDao mealsDao, final FoodInfoDao foodInfoDao, final AddMealTransaction addMealTransaction, final PersonalConfiguration personalConfiguration) {
        this.mealsDao = mealsDao;
        this.foodInfoDao = foodInfoDao;
        this.addMealTransaction = addMealTransaction;
        this.personalConfiguration = personalConfiguration;
    }

    @Override
    public List<Meal> getMealsOfDay(final LocalDate date) {
        return mealsDao.getMealsOfDay(date);
    }

    @Override
    public CaloriesForDay countCaloriesLeftForToday(final LocalDate date) {
        final CaloriesForDay caloriesForDay = new CaloriesForDay();
        caloriesForDay.caloriesEaten = foodInfoDao.caloriesOfDay(date);
        caloriesForDay.dailyCaloriesAmount = personalConfiguration.getDailyCaloriesNorm();
        return caloriesForDay;
    }

    @Override
    public List<FoodInfo> getFoodInfo(final String mealId) {
        return foodInfoDao.listFoodInfo(mealId);
    }

    @Override
    public void deleteMeal(final String mealId) {
        mealsDao.deleteMeal(mealId);
    }

    @Override
    public String addMeal(final AddMealRequest request) {
        addMealTransaction.insertMeal(request.meal, request.foodInfo);
        return request.meal.getNoteId();
    }

    @Override
    public Meal getMeal(final String mealId) {
        return mealsDao.getMeal(mealId);
    }

    @Override
    public void updateMeal(final Meal meal) {
        mealsDao.updateMeal(meal);
    }
}
