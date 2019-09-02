package ru.marina.notes.exercises;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.marina.notes.MyParamConverterProvider;

import javax.ws.rs.core.Application;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SportActivityRESTTest extends JerseyTest {
    private final SportActivity sportActivity = new SportActivity("activityId", "title", "text", LocalDate.of(2019, 01, 01),
            Timestamp.from(Instant.now()), LocalTime.of(9, 20), LocalTime.of(10, 40));
    private final Exercise exercise1 = new Exercise("exerciseId1", "activityId", 50, 10, 3, Toughness.EASY);
    private final Exercise exercise2 = new Exercise("exerciseId2", "activityId", 50, 10, 3, Toughness.HARD);

    private SportActivityDao sportActivityDao;
    private ExercisesDao exercisesDao;
    private AddSportActivityTransaction addSportActivityTransaction;

    public SportActivityREST createClient() {
        return WebResourceFactory.newResource(SportActivityREST.class, target());
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        sportActivityDao = mock(SportActivityDao.class);
        exercisesDao = mock(ExercisesDao.class);
        addSportActivityTransaction = mock(AddSportActivityTransaction.class);

        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        return new ResourceConfig()
                .register(new SportActivityRESTImplementation(sportActivityDao, exercisesDao, addSportActivityTransaction))
                .register(jacksonJaxbJsonProvider)
                .register(MyParamConverterProvider.class);
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        config.register(jacksonJaxbJsonProvider);
    }

    @Test
    public void testAddSportActivity() {
        SportActivityREST.AddSportActivityRequest request = new SportActivityREST.AddSportActivityRequest();
        request.sportActivity = sportActivity;
        request.exercises = Arrays.asList(exercise1, exercise2);
        final String activityId = createClient().addSportActivity(request);

        verify(addSportActivityTransaction).insertSportActivity(sportActivity, Arrays.asList(exercise1, exercise2));

        assertEquals("activityId", activityId);
    }

    @Test
    public void testGetSportActivity() {
        when(sportActivityDao.getSportActivity(any())).thenReturn(sportActivity);

        final SportActivity sport = createClient().getSportActivity(sportActivity.getNoteId());

        verify(sportActivityDao).getSportActivity(sportActivity.getNoteId());

        assertEquals(sportActivity, sport);

    }

    @Test
    public void testUpdateSportActivity() {
        createClient().updateSportActivity(sportActivity);

        verify(sportActivityDao).updateSportActivity(sportActivity);
    }

    @Test
    public void testGetSportActivitiesOfDay() {
        when(sportActivityDao.getSportActivitiesOfDay(any())).thenReturn(Collections.singletonList(sportActivity));

        final List<SportActivity> activities = createClient().getActivitiesOfDay(LocalDate.of(2019, 01, 01));

        verify(sportActivityDao).getSportActivitiesOfDay(LocalDate.of(2019, 01, 01));
        assertEquals(Collections.singletonList(sportActivity), activities);
    }

    @Test
    public void testGetExercises() {
        when(exercisesDao.listExercises(sportActivity.getNoteId())).thenReturn(Arrays.asList(exercise1, exercise2));

        final List<Exercise> list = createClient().getExercises(sportActivity.getNoteId());

        verify(exercisesDao).listExercises(sportActivity.getNoteId());

        assertEquals(Arrays.asList(exercise1, exercise2), list);
    }

    @Test
    public void testDeleteSportActivity() {
        createClient().deleteSportActivity(sportActivity.getNoteId());

        verify(sportActivityDao).deleteSportActivity(sportActivity.getNoteId());
    }

}
