package ru.marina.notes.meals;

import ru.marina.notes.Note;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Meal extends Note {

    public Meal(final String mealId, final String title, final String text, final LocalDate date, final Timestamp creationTime) {
        super(mealId, title, text, date, creationTime);
    }

    public Meal() {
    }
}
