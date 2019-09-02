package ru.marina.notes.meals;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(FoodInfo.class)
public interface FoodInfoDao {
    @SqlUpdate("INSERT INTO food_info (food_info_id, meal_id, product, portion_weight, calories) VALUES (:foodInfoId, :mealId, :product, :portionWeight, :calories)")
    void addFoodInfo(@BindBean FoodInfo foodInfo);

    @SqlUpdate("UPDATE food_info SET meal_id = :mealId, product = :product, portion_weight = :portionWeight, calories = :calories WHERE food_info_id = :foodInfoId")
    void updateFoodInfo(@BindBean FoodInfo foodInfo);

    @SqlQuery("SELECT * FROM food_info WHERE food_info_id = :foodInfoId")
    FoodInfo getFoodInfo(@Bind("foodInfoId") String foodInfoId);

    @SqlUpdate("DELETE FROM food_info WHERE food_info_id = :foodInfoId")
    void deleteFoodInfo(@Bind("foodInfoId") String foodInfoId);

    @SqlQuery("SELECT * FROM food_info WHERE meal_id = :mealId")
    List<FoodInfo> listFoodInfo(@Bind("mealId") String mealId);

    @SqlQuery("SELECT SUM(calories) FROM food_info INNER JOIN meals ON food_info.meal_id = meals.meal_id WHERE date = :date")
    int caloriesOfDay(@Bind("date") LocalDate date);
}
