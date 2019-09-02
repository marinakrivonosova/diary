package ru.marina.notes.healthparams;

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

class HealthParametersDaoTest {
    private HealthParametersDao healthParamDao;

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

        healthParamDao = jdbi.onDemand(HealthParametersDao.class);
    }

    @Test
    void crud() {
        final HealthParameters healthParam = new HealthParameters("noteId", "My health params", "Good Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), 80, 110, 50, "Good", "85-60-91");
        assertNull(healthParamDao.getHealthParameters(healthParam.getNoteId()));

        // create
        healthParamDao.addHealthParameters(healthParam);
        assertEquals(healthParam, healthParamDao.getHealthParameters(healthParam.getNoteId()));

        // update
        final HealthParameters updatedHealthParameters = new HealthParameters(healthParam.getNoteId(), "Health params", "Awesome Day", LocalDate.of(2018, 10, 17), Timestamp.from(Instant.now()), 78, 104, 52, "Awesome", "85-62-91");
        healthParamDao.updateHealthParameters(updatedHealthParameters);
        assertEquals(updatedHealthParameters, healthParamDao.getHealthParameters(healthParam.getNoteId()));


        // delete
        healthParamDao.deleteHealthParameters(healthParam.getNoteId());
        assertNull(healthParamDao.getHealthParameters(healthParam.getNoteId()));
    }

    @Test
    void dateTest() {
        final HealthParameters healthParam1 = new HealthParameters("noteId1", "My health params", "Good Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), 80, 110, 50, "Good", "85-60-91");

        assertNull(healthParamDao.getHealthParameters(healthParam1.getNoteId()));

        healthParamDao.addHealthParameters(healthParam1);

        final HealthParameters healthParam2 = new HealthParameters("noteId2", "My health params", "Good Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), 80, 110, 50, "Good", "85-60-91");
        final HealthParameters healthParam3 = new HealthParameters("noteId3", "My health params", "Good Day", LocalDate.of(2018, 05, 12), Timestamp.from(Instant.now()), 80, 110, 50, "Good", "85-60-91");
        healthParamDao.addHealthParameters(healthParam2);
        healthParamDao.addHealthParameters(healthParam3);
        List<HealthParameters> healthParametersOfDay = healthParamDao.getHeartParametersOfDay(LocalDate.of(2018, 06, 12));

        assertEquals(Arrays.asList(healthParam1, healthParam2), healthParametersOfDay);
    }
}