package com.davivienda.movies.model;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy","Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crime","Crimen"),
    ANIMACION("Animation","Animacion");
    //tratamiento para poder recibir las categorias que vienen diferente de las series.
  private String categoriaOmdb;
  private String categoriaSpanol;
  Categoria(String categoriaOmdb, String categoriaSpanol){
      this.categoriaOmdb=categoriaOmdb;
      this.categoriaSpanol=categoriaSpanol;
  }
  //
    public static Categoria fromString(String text) {//trallendo la categoria para transformar
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
    //verifica si el elemento que esta siendo ingresado puede ser igual o parecido a un elemento de nuestro enum Categoria
    public static Categoria fromSpanol(String text) {//trallendo la categoria para transformar
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaSpanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
