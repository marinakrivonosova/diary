package ru.marina.notes.meals;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Handles;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MealDaoTest {
    private MealDao mealDao;

    @BeforeEach
    void setup() throws Exception {
        final Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(Handles.class).setForceEndTransactions(false); // TODO do we need it?

        jdbi.useHandle(handle -> {
            final Connection connection = handle.getConnection();
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new Liquibase("migration.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.update("test");
        });

        mealDao = jdbi.onDemand(MealDao.class);
    }

    @Test
    void crud() {
        final Meal meal = new Meal("mealId", "Lunch", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now()));
        assertNull(mealDao.getMeal(meal.getNoteId()));

        // create
        mealDao.addMeal(meal);
        assertEquals(meal, mealDao.getMeal(meal.getNoteId()));

        // update
        final Meal updatedMeal = new Meal(meal.getNoteId(), "Breakfast", "Yammy", LocalDate.of(2018, 10, 13), Timestamp.from(Instant.now()));
        mealDao.updateMeal(updatedMeal);
        assertEquals(updatedMeal, mealDao.getMeal(meal.getNoteId()));

        // delete
        mealDao.deleteMeal(meal.getNoteId());
        assertNull(mealDao.getMeal(meal.getNoteId()));
    }

    @Test
    void dateTest() {
        final Meal meal1 = new Meal("mealId1", "Lunch", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now()));

        assertNull(mealDao.getMeal(meal1.getNoteId()));

        mealDao.addMeal(meal1);

        final Meal meal2 = new Meal("mealId2", "Lunch", "Mmmm", LocalDate.of(2018, 10, 13), Timestamp.from(Instant.now()));
        mealDao.addMeal(meal2);

        List<Meal> mealsOfDay = mealDao.getMealsOfDay(LocalDate.of(2018, 10, 13));

        assertEquals(Arrays.asList(meal2), mealsOfDay);
    }
}
