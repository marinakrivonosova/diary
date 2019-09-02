package ru.marina.notes;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(Note.class)
public interface NoteDao {
    @SqlUpdate("INSERT INTO notes (note_id, title, text, date, creation_time) VALUES (:noteId, :title, :text, :date, :creationTime)")
    void addNote(@BindBean Note note);

    @SqlUpdate("UPDATE notes SET title = :title, text = :text, date = :date, creation_time = :creationTime WHERE note_id = :noteId")
    void updateNote(@BindBean Note note);

    @SqlQuery("SELECT * FROM notes WHERE note_id = :noteId")
    Note getNote(@Bind("noteId") String noteId);

    @SqlUpdate("DELETE FROM notes WHERE note_id = :noteId")
    void deleteNote(@Bind("noteId") String noteId);

    @SqlQuery("SELECT * FROM notes WHERE date = :date")
    List<Note> getNotesOfDay(@Bind("date") LocalDate date);
}
