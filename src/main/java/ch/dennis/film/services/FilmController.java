package ch.dennis.film.services;

import ch.dennis.film.entities.Film;
import ch.dennis.film.repositories.FilmRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Path("/film")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;
    private final Logger logger = LogManager.getLogger(FilmController.class);

    // Grundlegende CRUD-Operationen bereits definiert
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Film findFilmById(@PathParam("id") Long id) {
        logger.info("Fetching film with ID {}", id);
        return filmRepository.findById(id).orElse(null);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<Film> getAllFilms() {
        logger.info("Fetching all films");
        return filmRepository.findAll();
    }

    @GET
    @Path("/by-regisseur/{regisseur}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<Film> getFilmsByRegisseur(@PathParam("regisseur") String regisseur) {
        logger.info("Fetching films by regisseur {}", regisseur);
        return filmRepository.findFilmsByRegisseur(regisseur);
    }

    @GET
    @Path("/by-genre/{genreId}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<Film> getFilmsByGenre(@PathParam("genreId") Long genreId) {
        logger.info("Fetching films by genre ID {}", genreId);
        return filmRepository.findFilmsByGenreId(genreId);
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response getCountOfFilms() {
        try {
            long count = filmRepository.count();
            return Response.ok("Total films: " + count).build();
        } catch (RuntimeException ex) {
            logger.error("Error counting films: {}", ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Database Error: " + ex.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/between-dates")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<Film> getFilmsBetweenDates(@QueryParam("start") Date start, @QueryParam("end") Date end) {
        logger.info("Fetching films between dates {} and {}", start, end);
        return filmRepository.findFilmsBetweenDates(start, end);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Admin", "Poster"})
    public Response createFilm(Film film) {
        try {
            Film savedFilm = filmRepository.save(film);
            logger.info("Created film with ID {}", savedFilm.getId());
            return Response.status(Response.Status.CREATED).entity(savedFilm).build();
        } catch (Exception e) {
            logger.error("Error creating film: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    public Response updateFilm(@PathParam("id") Long id, Film filmDetails) {
        return filmRepository.findById(id).map(existingFilm -> {
            existingFilm.setTitel(filmDetails.getTitel());
            existingFilm.setRegisseur(filmDetails.getRegisseur());
            existingFilm.setErscheinungsdatum(filmDetails.getErscheinungsdatum());
            existingFilm.setLaufzeit(filmDetails.getLaufzeit());
            existingFilm.setBewertung(filmDetails.getBewertung());
            existingFilm.setGenre(filmDetails.getGenre());
            filmRepository.save(existingFilm);
            logger.info("Updated film with ID {}", id);
            return Response.ok(existingFilm).build();
        }).orElseGet(() -> {
            logger.error("Film with ID {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        });
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    public Response deleteFilm(@PathParam("id") Long id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            logger.info("Deleted film with ID {}", id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            logger.error("Film with ID {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
