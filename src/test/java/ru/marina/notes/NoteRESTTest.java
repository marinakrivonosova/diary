package ru.marina.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Application;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NoteRESTTest extends JerseyTest {
    private final Note note = new Note("noteId", "text", "title", LocalDate.of(2018, 10, 14), Timestamp.from(Instant.now()));

    private NoteDao noteDao;

    public NoteREST createClient() {
        return WebResourceFactory.newResource(NoteREST.class, target());
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        noteDao = mock(NoteDao.class);

        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        return new ResourceConfig()
                .register(new NoteRESTImplementation(noteDao))
                .register(MyParamConverterProvider.class)
                .register(jacksonJaxbJsonProvider);
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        final JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        config.register(jacksonJaxbJsonProvider);
    }

    @Test
    public void testAddNote() {
        final String noteId = createClient().addNote(note);

        verify(noteDao).addNote(note);

        assertEquals("noteId", noteId);
    }

    @Test
    public void testGetNote() {
        when(noteDao.getNote(any())).thenReturn(note);

        final Note resultNote = createClient().getNote(note.getNoteId());

        verify(noteDao).getNote(note.getNoteId());

        assertEquals(note, resultNote);
    }

    @Test
    public void testUpdateNote() {
        createClient().updateNote(note);

        verify(noteDao).updateNote(note);
    }

    @Test
    public void testGetNotesIOfDay() {
        when(noteDao.getNotesOfDay(any())).thenReturn(Collections.singletonList(note));

        final List<Note> listNotes = createClient().getNotesOfDay(LocalDate.of(2018, 10, 14));

        verify(noteDao).getNotesOfDay(LocalDate.of(2018, 10, 14));
        assertEquals(Collections.singletonList(note), listNotes);
    }

    @Test
    public void testDelete() {
        createClient().deleteNote(note.getNoteId());

        verify(noteDao).deleteNote(note.getNoteId());
    }
}