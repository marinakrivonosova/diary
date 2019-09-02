package ru.marina.notes.exercises;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Exercise.class)
public interface ExercisesDao {
    @SqlUpdate("INSERT INTO exercise (exercise_id, activity_id, weight, amount_of_repeats, amount_of_cycles, toughness)" +
            "VALUES (:exerciseId, :activityId, :weight, :amountOfRepeats, :amountOfCycles, :toughness)")
    void addExercises(@BindBean Exercise exercise);

    @SqlUpdate("UPDATE exercise SET activity_id = :activityId, weight = :weight, amount_of_repeats = :amountOfRepeats," +
            "amount_of_cycles = :amountOfCycles, toughness = :toughness WHERE exercise_id = :exerciseId")
    void updateExercise(@BindBean Exercise exercise);

    @SqlQuery("SELECT *  FROM exercise WHERE exercise_id = :exerciseId")
    Exercise getExercise(@Bind("exerciseId") String exerciseId);

    @SqlUpdate("DELETE FROM exercise WHERE exercise_id = :exerciseId")
    void deleteExercise(@Bind("exerciseId") String exerciseId);

    @SqlQuery("SELECT * FROM exercise WHERE activity_id = :activityId")
    List<Exercise> listExercises(@Bind("activityId") String activityId);
}