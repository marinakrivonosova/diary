package ru.marina.notes.exercises;

import java.util.Objects;

public class Exercise {
    private String exerciseId;
    private String activityId;
    private int weight;
    private int amountOfRepeats;
    private int amountOfCycles;
    private Toughness toughness;

    public Exercise(final String exerciseId, final String activityId, final int weight, final int amountOfRepeats, final int amountOfCycles, final Toughness toughness) {
        this.exerciseId = exerciseId;
        this.activityId = activityId;
        this.weight = weight;
        this.amountOfRepeats = amountOfRepeats;
        this.amountOfCycles = amountOfCycles;
        this.toughness = toughness;
    }

    public Exercise() {
    }

    public void setExerciseId(final String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public void setAmountOfRepeats(final int amountOfRepeats) {
        this.amountOfRepeats = amountOfRepeats;
    }

    public void setAmountOfCycles(final int amountOfCycles) {
        this.amountOfCycles = amountOfCycles;
    }

    public void setToughness(final Toughness toughness) {
        this.toughness = toughness;
    }

    public String getActivityId() {
        return activityId;
    }

    public int getWeight() {
        return weight;
    }

    public int getAmountOfRepeats() {
        return amountOfRepeats;
    }

    public int getAmountOfCycles() {
        return amountOfCycles;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public Toughness getToughness() {
        return toughness;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Exercise exercise = (Exercise) o;
        return weight == exercise.weight &&
                amountOfRepeats == exercise.amountOfRepeats &&
                amountOfCycles == exercise.amountOfCycles &&
                Objects.equals(exerciseId, exercise.exerciseId) &&
                Objects.equals(activityId, exercise.activityId) &&
                toughness == exercise.toughness;
    }

    @Override
    public int hashCode() {

        return Objects.hash(exerciseId, activityId, weight, amountOfRepeats, amountOfCycles, toughness);
    }
}
