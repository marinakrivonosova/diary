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

class SportActivityDaoTest {
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

        sportActivityDao = jdbi.onDemand(SportActivityDao.class);
    }

    @Test
    void crud() {
        final SportActivity sportActivity = new SportActivity("noteId", "LegDay", "Hard Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), LocalTime.of(10, 20), LocalTime.of(11, 40));
        assertNull(sportActivityDao.getSportActivity(sportActivity.getNoteId()));

        // create
        sportActivityDao.addSportActivity(sportActivity);
        assertEquals(sportActivity, sportActivityDao.getSportActivity(sportActivity.getNoteId()));

        // update
        final SportActivity updateSportActivity = new SportActivity(sportActivity.getNoteId(), "Back day", "Easy Day", LocalDate.of(2018, 10, 17), Timestamp.from(Instant.now()), LocalTime.of(8, 0), LocalTime.of(10, 0));
        sportActivityDao.updateSportActivity(updateSportActivity);
        assertEquals(updateSportActivity, sportActivityDao.getSportActivity(sportActivity.getNoteId()));

        // delete
        sportActivityDao.deleteSportActivity(sportActivity.getNoteId());
        assertNull(sportActivityDao.getSportActivity(sportActivity.getNoteId()));
    }

    @Test
    void DateTest() {
        final SportActivity sportActivity1 = new SportActivity("noteId1", "LegDay", "Hard Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), LocalTime.of(10, 20), LocalTime.of(11, 40));

        assertNull(sportActivityDao.getSportActivity(sportActivity1.getNoteId()));

        final SportActivity sportActivity2 = new SportActivity("noteId2", "LegDay", "Hard Day", LocalDate.of(2018, 07, 12), Timestamp.from(Instant.now()), LocalTime.of(10, 20), LocalTime.of(11, 40));
        final SportActivity sportActivity3 = new SportActivity("noteId3", "LegDay", "Hard Day", LocalDate.of(2018, 06, 11), Timestamp.from(Instant.now()), LocalTime.of(10, 20), LocalTime.of(11, 40));
        final SportActivity sportActivity4 = new SportActivity("noteId4", "LegDay", "Hard Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()), LocalTime.of(10, 20), LocalTime.of(11, 40));
        sportActivityDao.addSportActivity(sportActivity1);
        sportActivityDao.addSportActivity(sportActivity2);
        sportActivityDao.addSportActivity(sportActivity3);
        sportActivityDao.addSportActivity(sportActivity4);

        List<SportActivity> sportActivitiesOfDay = sportActivityDao.getSportActivitiesOfDay(LocalDate.of(2018, 06, 12));

        assertEquals(Arrays.asList(sportActivity1, sportActivity4), sportActivitiesOfDay);
    }

}