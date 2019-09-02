package ru.marina.notes;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

public class Note {
    private String noteId;
    private String title;
    private String text;
    private LocalDate date;
    private Timestamp creationTime;

    public Note(final String noteId, final String title, final String text, final LocalDate date, final Timestamp creationTime) {
        this.noteId = noteId;
        this.title = title;
        this.text = text;
        this.date = date;
        this.creationTime = creationTime;
    }

    public Note() {
    }

    public void setNoteId(final String note_id) {
        this.noteId = note_id;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public void setCreationTime(final Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public String getNoteId() {
        return noteId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Note note = (Note) o;
        return Objects.equals(noteId, note.noteId) &&
                Objects.equals(title, note.title) &&
                Objects.equals(text, note.text) &&
                Objects.equals(date, note.date) &&
                Objects.equals(creationTime, note.creationTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(noteId, title, text, date, creationTime);
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId='" + noteId + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", creationTime=" + creationTime +
                '}';
    }
}
