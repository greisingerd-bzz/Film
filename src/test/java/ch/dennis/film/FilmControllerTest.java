package ch.dennis.film;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.dennis.film.entities.Film;
import ch.dennis.film.entities.Genre;
import ch.dennis.film.repositories.FilmRepository;
import ch.dennis.film.services.FilmController;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class FilmControllerTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmController filmController;

    private Film film;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        film = new Film();
        film.setId(1);
        film.setTitel("The Matrix");
        film.setRegisseur("Lana Wachowski");
        film.setErscheinungsdatum(new Date());
        film.setLaufzeit(120);
        film.setBewertung(8.5);
        film.setGenre(null);
    }

    // Tests for findFilmById
    @Test
    void findFilmById_Positive() {
        when(filmRepository.findById(1L)).thenReturn(Optional.of(film));
        Film result = filmController.findFilmById(1L);
        assertNotNull(result);
        assertEquals("The Matrix", result.getTitel());
    }

    @Test
    void findFilmById_Negative() {
        when(filmRepository.findById(1L)).thenReturn(Optional.empty());
        Film result = filmController.findFilmById(1L);
        assertNull(result);
    }

    @Test
    void findFilmById_Boundary() {
        when(filmRepository.findById(Long.MAX_VALUE)).thenReturn(Optional.empty());
        Film result = filmController.findFilmById(Long.MAX_VALUE);
        assertNull(result);
    }

    // Tests for getAllFilms
    @Test
    void getAllFilms_Positive() {
        when(filmRepository.findAll()).thenReturn(Arrays.asList(film));
        List<Film> result = filmController.getAllFilms();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getAllFilms_Negative() {
        when(filmRepository.findAll()).thenReturn(Arrays.asList());
        List<Film> result = filmController.getAllFilms();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFilms_Boundary() {
        List<Film> result = filmController.getAllFilms();
        assertNotNull(result);
    }

    // Tests for getFilmsByRegisseur
    @Test
    void getFilmsByRegisseur_Positive() {
        when(filmRepository.findFilmsByRegisseur("Lana Wachowski")).thenReturn(Arrays.asList(film));
        List<Film> result = filmController.getFilmsByRegisseur("Lana Wachowski");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getFilmsByRegisseur_Negative() {
        when(filmRepository.findFilmsByRegisseur("Lana Wachowski")).thenReturn(Arrays.asList());
        List<Film> result = filmController.getFilmsByRegisseur("Lana Wachowski");
        assertTrue(result.isEmpty());
    }

    @Test
    void getFilmsByRegisseur_Boundary() {
        when(filmRepository.findFilmsByRegisseur("")).thenReturn(Arrays.asList());
        List<Film> result = filmController.getFilmsByRegisseur("");
        assertTrue(result.isEmpty());
    }

    // Tests for createFilm
    @Test
    void createFilm_Positive() {
        when(filmRepository.save(any(Film.class))).thenReturn(film);
        Response result = filmController.createFilm(film);
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
    }

    @Test
    void createFilm_Negative() {
        when(filmRepository.save(any(Film.class))).thenThrow(new RuntimeException("Database Error"));
        Response result = filmController.createFilm(film);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), result.getStatus());
    }

    @Test
    void createFilm_Boundary() {
        Film newFilm = new Film();
        when(filmRepository.save(newFilm)).thenReturn(newFilm);
        Response result = filmController.createFilm(newFilm);
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
    }

    // Tests for updateFilm
    @Test
    void updateFilm_Positive() {
        when(filmRepository.findById(anyLong())).thenReturn(Optional.of(film));
        when(filmRepository.save(any(Film.class))).thenReturn(film);
        Response result = filmController.updateFilm(1L, film);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
    }

    @Test
    void updateFilm_Negative() {
        when(filmRepository.findById(anyLong())).thenReturn(Optional.empty());
        Response result = filmController.updateFilm(1L, film);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    @Test
    void updateFilm_Boundary() {
        Film updatedFilm = new Film();
        updatedFilm.setId(1);  // Existing ID
        updatedFilm.setTitel("New Title");
        when(filmRepository.findById(1L)).thenReturn(Optional.of(film));
        when(filmRepository.save(updatedFilm)).thenReturn(updatedFilm);
        Response result = filmController.updateFilm(1L, updatedFilm);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("New Title", ((Film)result.getEntity()).getTitel());
    }

    // Tests for deleteFilm
    @Test
    void deleteFilm_Positive() {
        when(filmRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(filmRepository).deleteById(anyLong());
        Response result = filmController.deleteFilm(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());
    }

    @Test
    void deleteFilm_Negative() {
        when(filmRepository.existsById(anyLong())).thenReturn(false);
        Response result = filmController.deleteFilm(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    @Test
    void deleteFilm_Boundary() {
        when(filmRepository.existsById(Long.MAX_VALUE)).thenReturn(false);
        Response result = filmController.deleteFilm(Long.MAX_VALUE);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    // Tests for getFilmsByGenre
    @Test
    void getFilmsByGenre_Positive() {
        Genre genre = new Genre();
        genre.setId(1);
        film.setGenre(genre);

        List<Film> films = Arrays.asList(film);
        when(filmRepository.findFilmsByGenreId(1L)).thenReturn(films);
        List<Film> result = filmController.getFilmsByGenre(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getGenre().getId().intValue());
    }

    @Test
    void getFilmsByGenre_Negative() {
        when(filmRepository.findFilmsByGenreId(999L)).thenReturn(Collections.emptyList());
        List<Film> result = filmController.getFilmsByGenre(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getFilmsByGenre_Boundary() {
        List<Film> films = new ArrayList<>();
        films.add(film);
        when(filmRepository.findFilmsByGenreId(Long.MIN_VALUE)).thenReturn(films);
        List<Film> result = filmController.getFilmsByGenre(Long.MIN_VALUE);
        assertFalse(result.isEmpty());
    }

    // Tests for getFilmsBetweenDates
    @Test
    void getFilmsBetweenDates_Positive() {
        List<Film> films = Arrays.asList(film);
        when(filmRepository.findFilmsBetweenDates(any(Date.class), any(Date.class))).thenReturn(films);
        List<Film> result = filmController.getFilmsBetweenDates(new Date(), new Date());
        assertFalse(result.isEmpty());
    }

    @Test
    void getFilmsBetweenDates_Negative() {
        when(filmRepository.findFilmsBetweenDates(any(Date.class), any(Date.class))).thenReturn(Collections.emptyList());
        List<Film> result = filmController.getFilmsBetweenDates(new Date(), new Date());
        assertTrue(result.isEmpty());
    }

    @Test
    void getFilmsBetweenDates_Boundary() {
        List<Film> films = new ArrayList<>();
        films.add(film);
        when(filmRepository.findFilmsBetweenDates(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE))).thenReturn(films);
        List<Film> result = filmController.getFilmsBetweenDates(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE));
        assertFalse(result.isEmpty());
    }

    // Tests for getCountOfFilms
    @Test
    void getCountOfFilms_Positive() {
        when(filmRepository.count()).thenReturn(10L);
        Response result = filmController.getCountOfFilms();
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("Total films: 10", result.getEntity());
    }

    @Test
    void getCountOfFilms_Negative() {
        when(filmRepository.count()).thenReturn(0L);
        Response result = filmController.getCountOfFilms();
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        // This is to check if handling of 0 count is properly managed
        assertEquals("Total films: 0", result.getEntity());
    }

    @Test
    void getCountOfFilms_Boundary() {
        when(filmRepository.count()).thenReturn(0L);
        Response result = filmController.getCountOfFilms();
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("Total films: 0", result.getEntity());
    }

    // Weiterführende Tests für updateFilm
    @Test
    void updateFilm_Negative_NoFilmFound() {
        when(filmRepository.findById(1L)).thenReturn(Optional.empty());
        Response result = filmController.updateFilm(1L, new Film());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    @Test
    void updateFilm_Boundary_EmptyFields() {
        Film existingFilm = new Film();
        existingFilm.setId(1);
        Film emptyDetails = new Film();
        when(filmRepository.findById(1L)).thenReturn(Optional.of(existingFilm));
        when(filmRepository.save(any(Film.class))).thenReturn(existingFilm);

        Response result = filmController.updateFilm(1L, emptyDetails);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertNotNull(result.getEntity());
    }

    // Weiterführende Tests für deleteFilm
    @Test
    void deleteFilm_Negative_NoFilmFound() {
        when(filmRepository.existsById(anyLong())).thenReturn(false);
        Response result = filmController.deleteFilm(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), result.getStatus());
    }

    @Test
    void deleteFilm_Boundary_MaxIdValue() {
        when(filmRepository.existsById(Long.MAX_VALUE)).thenReturn(true);
        doNothing().when(filmRepository).deleteById(Long.MAX_VALUE);

        Response result = filmController.deleteFilm(Long.MAX_VALUE);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());
    }

    // Weiterführende Tests für getFilmsByGenre
    @Test
    void getFilmsByGenre_Negative_NoFilmsFound() {
        when(filmRepository.findFilmsByGenreId(999L)).thenReturn(Collections.emptyList());
        List<Film> result = filmController.getFilmsByGenre(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getFilmsByGenre_Boundary_MinIdValue() {
        List<Film> films = Arrays.asList(new Film());
        when(filmRepository.findFilmsByGenreId(Long.MIN_VALUE)).thenReturn(films);
        List<Film> result = filmController.getFilmsByGenre(Long.MIN_VALUE);
        assertFalse(result.isEmpty());
    }

    // Additional tests for getCountOfFilms
    @Test
    void getCountOfFilms_Boundary_NoFilms() {
        when(filmRepository.count()).thenReturn(0L);
        Response result = filmController.getCountOfFilms();
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("Total films: 0", result.getEntity());
    }

    @Test
    void getCountOfFilms_Negative_ExceptionThrown() {
        when(filmRepository.count()).thenThrow(new RuntimeException("Database Error"));
        Response result = filmController.getCountOfFilms();
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), result.getStatus());
        assertTrue(result.getEntity().toString().contains("Database Error"),
                "Expected error message to include 'Database Error'");
    }

    // Additional test for getFilmsByRegisseur handling special characters
    @Test
    void getFilmsByRegisseur_SpecialCharacters() {
        Film filmWithSpecialChar = new Film();
        filmWithSpecialChar.setId(2);
        filmWithSpecialChar.setTitel("Müller's Journey");
        filmWithSpecialChar.setRegisseur("Müller");

        when(filmRepository.findFilmsByRegisseur("Müller")).thenReturn(Arrays.asList(filmWithSpecialChar));
        List<Film> result = filmController.getFilmsByRegisseur("Müller");
        assertFalse(result.isEmpty());
        assertEquals("Müller's Journey", result.get(0).getTitel());
    }

}
