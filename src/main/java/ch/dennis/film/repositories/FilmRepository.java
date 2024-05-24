package ch.dennis.film.repositories;

import ch.dennis.film.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findFilmsByRegisseur(String regisseur);

    @Query("SELECT f FROM Film f WHERE f.genre.genreID = :genreId")
    List<Film> findFilmsByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT f FROM Film f WHERE f.erscheinungsdatum BETWEEN :start AND :end")
    List<Film> findFilmsBetweenDates(@Param("start") Date start, @Param("end") Date end);

    @Query("SELECT f FROM Film f WHERE f.bewertung BETWEEN :min AND :max")
    List<Film> findFilmsByRatingRange(@Param("min") Double minRating, @Param("max") Double maxRating);

    @Query("SELECT g.bezeichnung AS genre, COUNT(f) AS count FROM Film f JOIN f.genre g GROUP BY g.bezeichnung")
    Map<String, Long> countFilmsGroupedByGenre();
}

