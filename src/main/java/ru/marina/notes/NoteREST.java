package ru.marina.notes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface NoteREST {
    @POST
    String addNote(Note note);

    @GET
    @Path("{noteId}")
    Note getNote(@PathParam("noteId") String noteId);

    @PUT
    void updateNote(Note note);

    @GET
    @Path("search")
    List<Note> getNotesOfDay(@QueryParam("date") LocalDate localDate);

    @DELETE
    @Path("{noteId}")
    void deleteNote(@PathParam("noteId")String noteId);
}
