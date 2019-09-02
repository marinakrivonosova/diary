package ru.marina.notes.healthparams;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(HealthParameters.class)
public interface HealthParametersDao {
    @SqlUpdate("INSERT INTO health_parameters (health_param_id, title, text, date, creation_time, blood_pressure, heart_rate, weight, overall_feeling," +
            " body_parameters) VALUES (:noteId, :title, :text, :date, :creationTime, :bloodPressure, :heartRate, :weight, :overallFeeling, :bodyParameters)")
    void addHealthParameters(@BindBean HealthParameters healthParameters);

    @SqlUpdate("UPDATE health_parameters SET title = :title, text = :text, date = :date, creation_time = :creationTime, blood_pressure = :bloodPressure," +
            " weight = :weight, overall_feeling = :overallFeeling, body_parameters = :bodyParameters WHERE health_param_id = :noteId")
    void updateHealthParameters(@BindBean HealthParameters healthParameters);

    @SqlQuery("SELECT *, health_param_id AS note_id FROM health_parameters WHERE health_param_id = :noteId")
    HealthParameters getHealthParameters(@Bind("noteId") String healthParameterId);

    @SqlUpdate("DELETE FROM health_parameters WHERE health_param_id = :noteId")
    void deleteHealthParameters(@Bind("noteId") String healthParameterId);

    @SqlQuery("SELECT *, health_param_id AS note_id FROM health_parameters WHERE date = :date")
    List<HealthParameters> getHeartParametersOfDay(@Bind("date") LocalDate date);
}
