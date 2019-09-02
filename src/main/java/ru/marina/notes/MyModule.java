package ru.marina.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.archaius.ConfigProxyFactory;
import com.netflix.archaius.guice.ArchaiusModule;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.marina.notes.exercises.SportActivityREST;
import ru.marina.notes.exercises.SportActivityRESTImplementation;
import ru.marina.notes.healthparams.HealthParametersREST;
import ru.marina.notes.healthparams.HealthParametersRESTImplementation;
import ru.marina.notes.meals.MealsREST;
import ru.marina.notes.meals.MealsRESTImplementation;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ArchaiusModule());
        install(new DBModule());

        bind(MealsREST.class).to(MealsRESTImplementation.class);
        bind(HealthParametersREST.class).to(HealthParametersRESTImplementation.class);
        bind(SportActivityREST.class).to(SportActivityRESTImplementation.class);
        bind(NoteREST.class).to(NoteRESTImplementation.class);
    }

    @Provides
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Provides
    public JacksonJsonProvider jsonProvider(final ObjectMapper objectMapper) {
        final JacksonJaxbJsonProvider result = new JacksonJaxbJsonProvider();
        result.setMapper(objectMapper);
        return result;
    }

    @Provides
    public HttpServer provideHttpServer(final MealsREST mealsREST, final HealthParametersREST healthParametersREST,
                                        final SportActivityREST sportActivityREST, final NoteREST noteREST,
                                        final JacksonJsonProvider jsonProvider) {
        final URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
        final ResourceConfig config = new ResourceConfig()
                .register(mealsREST)
                .register(healthParametersREST)
                .register(sportActivityREST)
                .register(noteREST)
                .register(jsonProvider)
                .register(MyParamConverterProvider.class);
        return JdkHttpServerFactory.createHttpServer(baseUri, config, false);
    }

    @Provides
    public PersonalConfiguration personalConfiguration(final ConfigProxyFactory proxyFactory) {
        return proxyFactory.newProxy(PersonalConfiguration.class);
    }

}