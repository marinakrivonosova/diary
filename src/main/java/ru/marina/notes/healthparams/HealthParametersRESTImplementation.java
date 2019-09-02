package ru.marina.notes.healthparams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

public class HealthParametersRESTImplementation implements HealthParametersREST {
    private static final Logger logger = LoggerFactory.getLogger(HealthParametersRESTImplementation.class);

    private final HealthParametersDao healthParametersDao;

    @Inject
    public HealthParametersRESTImplementation(final HealthParametersDao healthParametersDao) {
        this.healthParametersDao = healthParametersDao;
    }

    @Override
    public String addHealthParameters(final HealthParameters healthParameters) {
        healthParametersDao.addHealthParameters(healthParameters);
        return healthParameters.getNoteId();
    }

    @Override
    public HealthParameters getHealthParameters(final String healthParameterId) {
        return healthParametersDao.getHealthParameters(healthParameterId);
    }

    @Override
    public void updateHealthParameters(final HealthParameters healthParameters) {
        healthParametersDao.updateHealthParameters(healthParameters);
    }

    @Override
    public List<HealthParameters> getHealthParamsOfDay(final LocalDate date) {
        return healthParametersDao.getHeartParametersOfDay(date);
    }

    @Override
    public void deleteHealthParameters(final String healthParameterId) {
        healthParametersDao.deleteHealthParameters(healthParameterId);
    }
}
