package com.davivienda.movies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;
//Hibernate
@Entity
@Table(name="peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto Incremento
    private Long id;
    @Column(unique = true)//para que el nombre no se repite, tambien personalizar el nombre para la BD
    private String titulo;
    private Integer ano_Streno;
    private String director;
    private Double evaluacion;
    private String poster;
    @Enumerated(EnumType.STRING)//stream->ambiente colaborativo, es
    private Categoria genero;
    private String actores;
    private String sinopsis;
    // Relación con Usuario
    @ManyToOne
    @JsonIgnore
    // evita que al serializar se incluya el objeto usuario y se quede en un ciclo infinito
    private Usuario usuario;


    public Pelicula() {
    }

    public Pelicula(DatosPelicula datosPelicula) {
        this.titulo = datosPelicula.titulo();
        this.ano_Streno = Integer.parseInt(datosPelicula.ano_Estreno());
        this.director = datosPelicula.director();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosPelicula.evaluacion())).orElse(0.0) ;
        //puede o no puede hacer la conversion
        this.poster = datosPelicula.poster();
        this.genero = Categoria.fromString(datosPelicula.genero().split(",")[0].trim());;
        this.actores = datosPelicula.actores();
        this.sinopsis = datosPelicula.sinopsis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Integer getAno_Streno() {
        return ano_Streno;
    }

    public void setAno_Streno(Integer ano_Streno) {
        this.ano_Streno = ano_Streno;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", ano_Streno=" + ano_Streno +
                ", director='" + director + '\'' +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", genero=" + genero +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                '}';
    }
}
