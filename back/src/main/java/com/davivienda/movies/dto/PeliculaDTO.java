package com.davivienda.movies.dto;

import com.davivienda.movies.model.Categoria;

public record PeliculaDTO(
        Long id,
        String titulo,
        Integer ano_Estreno,
        String director,
        Double evaluacion,
        String poster,
        Categoria genero,
        String actores,
        String sinopsis
) {
}
