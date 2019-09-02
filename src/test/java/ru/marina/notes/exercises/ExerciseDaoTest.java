package ru.marina.notes.exercises;

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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExerciseDaoTest {
    private ExercisesDao exercisesDao;
    private SportActivityDao sportActivityDao;

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
        exercisesDao = jdbi.onDemand(ExercisesDao.class);
        sportActivityDao = jdbi.onDemand(SportActivityDao.class);
    }

    @Test
    void crud() {
        final SportActivity sportActivity = new SportActivity("noteId", "LegDay", "Hard Day",
                LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()),
                LocalTime.of(10, 20), LocalTime.of(11, 40));
        sportActivityDao.addSportActivity(sportActivity);

        final Exercise exercise = new Exercise("exerciseId", sportActivity.getNoteId(), 50, 10, 5, Toughness.EASY);
        assertNull(exercisesDao.getExercise(exercise.getExerciseId()));

        exercisesDao.addExercises(exercise);
        assertEquals(exercise, exercisesDao.getExercise(exercise.getExerciseId()));

        final Exercise updatedExercise = new Exercise(exercise.getExerciseId(), exercise.getActivityId(), 80, 25, 3, Toughness.HARD);
        exercisesDao.updateExercise(updatedExercise);
        assertEquals(updatedExercise, exercisesDao.getExercise(updatedExercise.getExerciseId()));

        exercisesDao.deleteExercise(exercise.getExerciseId());
        assertNull(exercisesDao.getExercise(exercise.getExerciseId()));
    }

    @Test
    void testList() {
        final SportActivity sportActivity1 = new SportActivity("noteId1", "LegDay", "Hard Day",
                LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()),
                LocalTime.of(10, 20), LocalTime.of(11, 40));
        final SportActivity sportActivity2 = new SportActivity("noteId2", "BackDay", "Easy Day",
                LocalDate.of(2018, 06, 15), Timestamp.from(Instant.now()),
                LocalTime.of(11, 00), LocalTime.of(13, 00));
        sportActivityDao.addSportActivity(sportActivity1);
        sportActivityDao.addSportActivity(sportActivity2);

        final Exercise squats = new Exercise("exerciseId1", "noteId1", 40, 10, 3, Toughness.HARD);
        final Exercise jumps = new Exercise("exerciseId2", "noteId1", 50, 20, 1, Toughness.EASY);
        final Exercise pushUps = new Exercise("exerciseId3", "noteId2", 50, 15, 3, Toughness.EASY);
        exercisesDao.addExercises(squats);
        exercisesDao.addExercises(pushUps);
        exercisesDao.addExercises(jumps);

        final List<Exercise> exercises = exercisesDao.listExercises("noteId1");
        assertEquals(Arrays.asList(squats, jumps), exercises);
    }

    @Test
    void transactionsTest() {

    }
}