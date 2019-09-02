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

import static org.junit.jupiter.api.Assertions.*;

class FoodInfoDaoTest {
    private Jdbi jdbi;

    private FoodInfoDao foodInfoDao;
    private MealDao mealDao;

    @BeforeEach
    void setup() throws Exception {
        this.jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(Handles.class).setForceEndTransactions(false); // TODO do we need it?

        jdbi.useHandle(handle -> {
            final Connection connection = handle.getConnection();
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new Liquibase("migration.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.update("test");
        });

        foodInfoDao = jdbi.onDemand(FoodInfoDao.class);
        mealDao = jdbi.onDemand(MealDao.class);
    }

    @Test
    void crud() {
        final FoodInfo foodInfo = new FoodInfo("foodInfoId", "mealId", "Product", 100, 155);

        mealDao.addMeal(new Meal("mealId", "Lunch", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now())));
        assertNull(foodInfoDao.getFoodInfo(foodInfo.getFoodInfoId()));

        // create
        foodInfoDao.addFoodInfo(foodInfo);
        assertEquals(foodInfo, foodInfoDao.getFoodInfo(foodInfo.getFoodInfoId()));

        // update
        final FoodInfo updatedFoodInfo = new FoodInfo(foodInfo.getFoodInfoId(), foodInfo.getMealId(), "Apple", 321, 4321);
        foodInfoDao.updateFoodInfo(updatedFoodInfo);
        assertEquals(updatedFoodInfo, foodInfoDao.getFoodInfo(foodInfo.getFoodInfoId()));

        // delete
        foodInfoDao.deleteFoodInfo(foodInfo.getFoodInfoId());
        assertNull(foodInfoDao.getFoodInfo(foodInfo.getFoodInfoId()));
    }

    @Test
    void listFoodInfo() {
        // given
        mealDao.addMeal(new Meal("mealId1", "Breakfast", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now())));
        mealDao.addMeal(new Meal("mealId2", "Lunch", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now())));

        final FoodInfo apple = new FoodInfo("foodInfoId1", "mealId1", "Apple", 123, 100);
        final FoodInfo soup = new FoodInfo("foodInfoId2", "mealId2", "Soup", 234, 200);
        final FoodInfo tea = new FoodInfo("foodInfoId3", "mealId1", "Tea", 345, 300);
        foodInfoDao.addFoodInfo(apple);
        foodInfoDao.addFoodInfo(soup);
        foodInfoDao.addFoodInfo(tea);

        // when
        final List<FoodInfo> result = foodInfoDao.listFoodInfo("mealId1");

        // then
        assertEquals(Arrays.asList(apple, tea), result);
    }


    @Test
    void checkNotNullDBResponseForCalories(){
        assertEquals(0, foodInfoDao.caloriesOfDay(LocalDate.now()));

        mealDao.addMeal(new Meal("mealId1", "Breakfast", "Mmmm", LocalDate.now(), Timestamp.from(Instant.now())));
        mealDao.addMeal(new Meal("mealId2", "Lunch", "Mmmm", LocalDate.now(), Timestamp.from(Instant.now())));

        foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId1", "mealId1", "Apple", 123, 100));
        foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId2", "mealId2", "Soup", 234, 200));

        assertEquals(0,foodInfoDao.caloriesOfDay(LocalDate.of(2018,05, 14)));
    }

    void insert() {
        jdbi.useTransaction(handle -> {
            final MealDao mealDao = handle.attach(MealDao.class);
            mealDao.addMeal(new Meal("mealId", "Lunch", "Mmmm", LocalDate.of(2018, 10, 12), Timestamp.from(Instant.now())));
            final FoodInfoDao foodInfoDao = handle.attach(FoodInfoDao.class);
            foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId1", "mealId", "Product1", 110, 210));
            foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId2", "mealI", "Product2", 120, 220));
            foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId3", "mealId", "Product3", 130, 230));
            foodInfoDao.addFoodInfo(new FoodInfo("foodInfoId4", "mealId", "Product4", 140, 240));
        });
    }

    @Test
    void transactionsTest() {
        try {
            insert();
            fail("Exception expected");
        } catch (Exception e) {
        }
        assertNull(mealDao.getMeal("mealId"));
        assertNull(foodInfoDao.getFoodInfo("foodInfoId1"));
        assertNull(foodInfoDao.getFoodInfo("foodInfoId2"));
        assertNull(foodInfoDao.getFoodInfo("foodInfoId3"));
        assertNull(foodInfoDao.getFoodInfo("foodInfoId4"));
    }
}