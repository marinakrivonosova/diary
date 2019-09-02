package ru.marina.notes;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.marina.notes.meals.MealsRESTImplementation;

import java.time.LocalDate;
import java.util.List;

public class NoteRESTImplementation implements NoteREST {
    private static final Logger logger = LoggerFactory.getLogger(MealsRESTImplementation.class);

    private final NoteDao noteDao;

    @Inject
    public NoteRESTImplementation(final NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public String addNote(final Note note) {
        noteDao.addNote(note);
        return note.getNoteId();
    }

    @Override
    public Note getNote(final String noteId) {
        return noteDao.getNote(noteId);
    }

    @Override
    public void updateNote(final Note note) {
        noteDao.updateNote(note);
    }

    @Override
    public List<Note> getNotesOfDay(final LocalDate localDate) {
        return noteDao.getNotesOfDay(localDate);
    }

    @Override
    public void deleteNote(final String noteId) {
        noteDao.deleteNote(noteId);
    }
}
