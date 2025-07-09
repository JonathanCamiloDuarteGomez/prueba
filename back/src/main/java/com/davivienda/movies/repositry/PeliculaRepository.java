package com.davivienda.movies.repositry;

import com.davivienda.movies.model.Categoria;
import com.davivienda.movies.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
El Repository se encarga de la interacción con la base de datos.
El Service implementa la lógica de negocio y coordina el acceso a los datos.
El Controller expone los endpoints de la API y delega la lógica al Service.
*/
public interface PeliculaRepository extends JpaRepository<Pelicula,Long> {
    List<Pelicula> findByUsuarioId(Long id);
    List<Pelicula> findByGenero(Categoria categoria);
    Optional<Pelicula> findAllById(Long id);
}
