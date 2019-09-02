package ru.marina.notes.meals;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(Meal.class)
public interface MealDao {
    @SqlUpdate("INSERT INTO meals (meal_id, title, text, date, creation_time) VALUES (:noteId, :title, :text, :date, :creationTime)")
    void addMeal(@BindBean Meal meal);

    @SqlUpdate("UPDATE meals SET title = :title, text = :text, date = :date, creation_time = :creationTime WHERE meal_id = :noteId")
    void updateMeal(@BindBean Meal meal);

    @SqlQuery("SELECT *, meal_id AS note_id FROM meals WHERE meal_id = :noteId")
    Meal getMeal(@Bind("noteId") String mealId);

    @SqlUpdate("DELETE FROM meals WHERE meal_id = :noteId")
    void deleteMeal(@Bind("noteId") String mealId);

    @SqlQuery("SELECT *, meal_id AS note_id FROM meals WHERE date = :date")
    List<Meal> getMealsOfDay(@Bind("date") LocalDate date);
}
