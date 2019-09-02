package ru.marina.notes.healthparams;

import ru.marina.notes.Note;

import java.sql.Timestamp;
import java.time.LocalDate;

public class HealthParameters extends Note {
    private int bloodPressure;
    private int heartRate;
    private int weight;
    private String overallFeeling;
    private String bodyParameters;

    public HealthParameters(final String healthParameterId, final String title, final String text, final LocalDate date, final Timestamp creationTime,
                            final int bloodPressure, final int heartRate, final int weight, final String overallFeeling, final String bodyParameters) {
        super(healthParameterId, title, text, date, creationTime);
        this.bloodPressure = bloodPressure;
        this.bodyParameters = bodyParameters;
        this.heartRate = heartRate;
        this.overallFeeling = overallFeeling;
        this.weight = weight;

    }
    public HealthParameters(){}

    public void setBloodPressure(final int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public void setHeartRate(final int heartRate) {
        this.heartRate = heartRate;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public void setOverallFeeling(final String overallFeeling) {
        this.overallFeeling = overallFeeling;
    }

    public void setBodyParameters(final String bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getWeight() {
        return weight;
    }

    public String getOverallFeeling() {
        return overallFeeling;
    }

    public String getBodyParameters() {
        return bodyParameters;
    }
}
