package ch.dennis.film.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreID;

    @NotBlank(message = "Bezeichnung darf nicht leer sein")
    private String bezeichnung;

    @JsonIgnore
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Film> filme;

    // Getters and Setters
    public Integer getId() {
        return genreID;
    }

    public void setId(Integer id) {
        this.genreID = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public List<Film> getFilme() {
        return filme;
    }

    public void setFilme(List<Film> filme) {
        this.filme = filme;
    }
}