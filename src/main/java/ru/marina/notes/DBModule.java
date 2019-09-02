package ru.marina.notes;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.archaius.ConfigProxyFactory;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Handles;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.marina.notes.exercises.*;
import ru.marina.notes.healthparams.HealthParametersDao;
import ru.marina.notes.healthparams.HealthParametersREST;
import ru.marina.notes.healthparams.HealthParametersRESTImplementation;
import ru.marina.notes.meals.*;

import javax.inject.Singleton;
import java.sql.Connection;

public class DBModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public Jdbi jdbi(final DBConfiguration dbConfiguration) throws LiquibaseException {
        final Jdbi jdbi = Jdbi.create(dbConfiguration.getConnectionString());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(Handles.class).setForceEndTransactions(false); // TODO do we need it?

        jdbi.useHandle(handle -> {
            final Connection connection = handle.getConnection();
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new Liquibase("migration.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.update("prod");
        });

        return jdbi;
    }

    @Provides
    public DBConfiguration getDBConfiguration(final ConfigProxyFactory proxyFactory) {
        return proxyFactory.newProxy(DBConfiguration.class);
    }

    @Provides
    public MealDao mealDao(final Jdbi jdbi) {
        return jdbi.onDemand(MealDao.class);
    }

    @Provides
    public HealthParametersDao healthParametersDao(final Jdbi jdbi) {
        return jdbi.onDemand(HealthParametersDao.class);
    }

    @Provides
    public SportActivityDao sportActivityDao(final Jdbi jdbi) {
        return jdbi.onDemand(SportActivityDao.class);
    }

    @Provides
    public NoteDao noteDao(final Jdbi jdbi) {
        return jdbi.onDemand(NoteDao.class);
    }

    @Provides
    public FoodInfoDao foodInfoDao(final Jdbi jdbi) {
        return jdbi.onDemand(FoodInfoDao.class);
    }

    @Provides
    public ExercisesDao exercisesDao(final Jdbi jdbi) {
        return jdbi.onDemand(ExercisesDao.class);
    }

    @Provides
    public AddMealTransaction addMealTransaction(final Jdbi jdbi) {
        return jdbi.onDemand(AddMealTransaction.class);
    }

    @Provides
    public AddSportActivityTransaction addSportActivityTransaction(final Jdbi jdbi) {
        return jdbi.onDemand(AddSportActivityTransaction.class);
    }
}
