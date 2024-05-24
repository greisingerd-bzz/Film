package ch.dennis.film.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer filmID;

    @NotBlank(message = "Titel darf nicht leer sein")
    private String titel;

    private String regisseur;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date erscheinungsdatum;

    @Positive(message = "Laufzeit muss positiv sein")
    private Integer laufzeit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Bewertung muss positiv sein")
    @DecimalMax(value = "10.0", message = "Bewertung darf 10 nicht überschreiten")
    private Double bewertung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GenreID", referencedColumnName = "GenreID")
    private Genre genre;

    private Boolean aktiv = true;  // Standardmäßig auf 'true' gesetzt

    // Getters and Setters
    public Integer getId() {
        return filmID;
    }

    public void setId(Integer id) {
        this.filmID = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getRegisseur() {
        return regisseur;
    }

    public void setRegisseur(String regisseur) {
        this.regisseur = regisseur;
    }

    public Date getErscheinungsdatum() {
        return erscheinungsdatum;
    }

    public void setErscheinungsdatum(Date erscheinungsdatum) {
        this.erscheinungsdatum = erscheinungsdatum;
    }

    public Integer getLaufzeit() {
        return laufzeit;
    }

    public void setLaufzeit(Integer laufzeit) {
        this.laufzeit = laufzeit;
    }

    public Double getBewertung() {
        return bewertung;
    }

    public void setBewertung(Double bewertung) {
        this.bewertung = bewertung;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }
}
