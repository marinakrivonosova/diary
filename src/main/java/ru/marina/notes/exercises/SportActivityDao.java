package ru.marina.notes.exercises;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(SportActivity.class)
public interface SportActivityDao {
    @SqlUpdate("INSERT INTO sport_activities (activity_id, title, text, date, creation_time, start_time, finish_time) VALUES (:noteId, :title, :text, " +
            ":date, :creationTime, :startTime, :finishTime)")
    void addSportActivity(@BindBean SportActivity sportActivity);

    @SqlUpdate("UPDATE sport_activities SET title = :title, text = :text, date = :date, creation_time = :creationTime, start_time = :startTime, " +
            "finish_time = :finishTime WHERE activity_id = :noteId")
    void updateSportActivity(@BindBean SportActivity sportActivity);

    @SqlQuery("SELECT *, activity_id AS note_id FROM sport_activities WHERE activity_id = :noteId")
    SportActivity getSportActivity(@Bind("noteId") String activityId);

    @SqlUpdate("DELETE FROM sport_activities WHERE activity_id = :noteId")
    void deleteSportActivity(@Bind("noteId") String activityId);

    @SqlQuery("SELECT *, activity_id AS note_id FROM sport_activities WHERE date = :date")
    List<SportActivity> getSportActivitiesOfDay(@Bind("date") LocalDate date);
}
