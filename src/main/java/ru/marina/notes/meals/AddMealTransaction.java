package ru.marina.notes.meals;

import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface AddMealTransaction {
    @CreateSqlObject
    MealDao mealDao();

    @CreateSqlObject
    FoodInfoDao foodInfoDao();

    @Transaction
    default void insertMeal(final Meal meal, final List<FoodInfo> foodInfo) {
        mealDao().addMeal(meal);
        for (final FoodInfo food : foodInfo)
            foodInfoDao().addFoodInfo(food);
    }
}
