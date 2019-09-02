package ru.marina.notes;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Handles;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NoteDaoTest {
    private NoteDao noteDao;

    @BeforeEach
    void setup() throws Exception {
        final Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(Handles.class).setForceEndTransactions(false); // TODO do we need it?

        jdbi.useHandle(handle -> {
            final Connection connection = handle.getConnection();
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new Liquibase("migration.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.update("test");
        });

        noteDao = jdbi.onDemand(NoteDao.class);
    }

    @Test
    void crud() {
        final Note note = new Note("noteId", "MyDay", "Long Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()));
        assertNull(noteDao.getNote(note.getNoteId()));

        // create
        noteDao.addNote(note);
        assertEquals(note, noteDao.getNote(note.getNoteId()));

        // update
        final Note updatedNote = new Note(note.getNoteId(), "DayOff", "Party Day", LocalDate.of(2018, 10, 17), Timestamp.from(Instant.now()));
        noteDao.updateNote(updatedNote);
        assertEquals(updatedNote, noteDao.getNote(note.getNoteId()));


        // delete
        noteDao.deleteNote(note.getNoteId());
        assertNull(noteDao.getNote(note.getNoteId()));

    }

    @Test
    void dateTest() {
        final Note note1 = new Note("noteId1", "MyDay1", "Long Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()));

        assertNull(noteDao.getNote(note1.getNoteId()));

        final Note note2 = new Note("noteId2", "MyDay2", "Just Day", LocalDate.of(2018, 07, 12), Timestamp.from(Instant.now()));
        final Note note3 = new Note("noteId3", "MyDay3", "Long Day", LocalDate.of(2018, 05, 12), Timestamp.from(Instant.now()));
        final Note note4 = new Note("noteId4", "MyDay4", "Just Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()));
        final Note note5 = new Note("noteId5", "MyDay5", "Just Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()));
        final Note note6 = new Note("noteId6", "MyDay6", "Long Day", LocalDate.of(2018, 06, 13), Timestamp.from(Instant.now()));
        final Note note7 = new Note("noteId7", "MyDay7", "Long Day", LocalDate.of(2013, 06, 12), Timestamp.from(Instant.now()));
        final Note note8 = new Note("noteId8", "MyDay8", "Bad Day", LocalDate.of(2018, 06, 12), Timestamp.from(Instant.now()));
        noteDao.addNote(note1);
        noteDao.addNote(note2);
        noteDao.addNote(note3);
        noteDao.addNote(note4);
        noteDao.addNote(note5);
        noteDao.addNote(note6);
        noteDao.addNote(note7);
        noteDao.addNote(note8);

        List<Note> notesOfDay = noteDao.getNotesOfDay(LocalDate.of(2018, 06, 12));

        assertEquals(Arrays.asList(note1, note4, note5, note8), notesOfDay);
    }
}