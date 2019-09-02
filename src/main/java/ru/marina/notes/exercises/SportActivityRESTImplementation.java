package ru.marina.notes.exercises;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class SportActivityRESTImplementation implements SportActivityREST {
    private static final Logger logger = LoggerFactory.getLogger(SportActivityRESTImplementation.class);

    private final SportActivityDao sportActivityDao;
    private final ExercisesDao exercisesDao;
    public final AddSportActivityTransaction addSportActivityTransaction;


    @Inject
    public SportActivityRESTImplementation(final SportActivityDao sportActivityDao, final ExercisesDao exercisesDao, final AddSportActivityTransaction addSportActivityTransaction) {
        this.sportActivityDao = sportActivityDao;
        this.exercisesDao = exercisesDao;
        this.addSportActivityTransaction = addSportActivityTransaction;
    }

    @Override
    public String addSportActivity(final AddSportActivityRequest request) {
        addSportActivityTransaction.insertSportActivity(request.sportActivity, request.exercises);
        return request.sportActivity.getNoteId();
    }

    @Override
    public SportActivity getSportActivity(final String activityId) {
        return sportActivityDao.getSportActivity(activityId);
    }

    @Override
    public void updateSportActivity(final SportActivity sportActivity) {
        sportActivityDao.updateSportActivity(sportActivity);
    }

    @Override
    public List<SportActivity> getActivitiesOfDay(final LocalDate date) {
        return sportActivityDao.getSportActivitiesOfDay(date);
    }

    @Override
    public List<Exercise> getExercises(final String activityId) {
        return exercisesDao.listExercises(activityId);
    }

    @Override
    public void deleteSportActivity(final String activityId) {
        sportActivityDao.deleteSportActivity(activityId);
    }
}
