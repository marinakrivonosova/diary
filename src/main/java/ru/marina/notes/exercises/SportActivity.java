package ru.marina.notes.exercises;

import ru.marina.notes.Note;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class SportActivity extends Note {
    private LocalTime startTime;
    private LocalTime finishTime;

    public SportActivity(final String activityId, final String title, final String text, final LocalDate date, final Timestamp creationTime, final LocalTime startTime,
                         final LocalTime finishTime) {
        super(activityId, title, text, date, creationTime);
        this.startTime = startTime;
        this.finishTime = finishTime;


    }

    public SportActivity() {
    }

    public void setStartTime(final LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(final LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }
}
