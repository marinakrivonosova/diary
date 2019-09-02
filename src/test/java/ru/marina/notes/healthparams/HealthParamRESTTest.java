package ru.marina.notes.healthparams;

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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HealthParamRESTTest extends JerseyTest {
    private final HealthParameters healthParameters = new HealthParameters("healthParamId", "title", "text", LocalDate.of(2019, 05, 18), Timestamp.from(Instant.now()),
            120, 80, 60, "good", "94-70-92");

    private HealthParametersDao healthParametersDao;

    public HealthParametersREST createClient() {
        return WebResourceFactory.newResource(HealthParametersREST.class, target());
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
        healthParametersDao = mock(HealthParametersDao.class);

        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        return new ResourceConfig()
                .register(new HealthParametersRESTImplementation(healthParametersDao))
                .register(MyParamConverterProvider.class)
                .register(jacksonJaxbJsonProvider);
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        config.register(jacksonJaxbJsonProvider);
    }

    @Test
    public void testAddHealthParameters() {
        final String healthParamId = createClient().addHealthParameters(healthParameters);

        verify(healthParametersDao).addHealthParameters(healthParameters);

        assertEquals("healthParamId", healthParamId);
    }

    @Test
    public void getHealthParameters() {
        when(healthParametersDao.getHealthParameters(any())).thenReturn(healthParameters);

        final HealthParameters healthParameter = createClient().getHealthParameters(healthParameters.getNoteId());

        verify(healthParametersDao).getHealthParameters(healthParameters.getNoteId());

        assertEquals(healthParameters, healthParameter);
    }

    @Test
    public void testGetHealthParametersOfDay() {
        when(healthParametersDao.getHeartParametersOfDay(LocalDate.of(2019, 05, 18))).thenReturn(Collections.singletonList(healthParameters));

        List<HealthParameters> healthParametersList = createClient().getHealthParamsOfDay(LocalDate.of(2019, 05, 18));

        verify(healthParametersDao).getHeartParametersOfDay(LocalDate.of(2019, 05, 18));
        assertEquals(Collections.singletonList(healthParameters), healthParametersList);
    }

    @Test
    public void testUpdateHealthParameters() {
        createClient().updateHealthParameters(healthParameters);

        verify(healthParametersDao).updateHealthParameters(healthParameters);
    }

    @Test
    public void testDeleteHealthParameters() {
        createClient().deleteHealthParameters(healthParameters.getNoteId());

        verify(healthParametersDao).deleteHealthParameters(healthParameters.getNoteId());
    }
}
