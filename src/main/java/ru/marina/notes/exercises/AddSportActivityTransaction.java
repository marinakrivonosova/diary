package ru.marina.notes.exercises;

import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface AddSportActivityTransaction {
    @CreateSqlObject
    SportActivityDao sportActivityDao();

    @CreateSqlObject
    ExercisesDao exercisesDao();

    @Transaction
    default void insertSportActivity(final SportActivity sportActivity, final List<Exercise> exercises){
        sportActivityDao().addSportActivity(sportActivity);
        for (final Exercise exercise: exercises){
            exercisesDao().addExercises(exercise);
        }
    }
}
