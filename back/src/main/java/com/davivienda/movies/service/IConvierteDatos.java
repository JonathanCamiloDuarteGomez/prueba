package com.davivienda.movies.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
