package com.davivienda.movies.principal;

import com.davivienda.movies.model.*;
import com.davivienda.movies.repositry.PeliculaRepository;
import com.davivienda.movies.repositry.UsuarioRepository;
import com.davivienda.movies.service.ConsumoAPI;
import com.davivienda.movies.service.ConvierteDatos;

import java.util.*;


public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private String API_KEY;
    private ConvierteDatos conversor = new ConvierteDatos();
    //lo de abajo, esto primero se creo en el principal que es una clase creada por el spring y que puede manejar inyeccion de depencias
    private PeliculaRepository repositorio;
    private UsuarioRepository usuarioRepository;
    private List<Pelicula> peliculas;
    //private Optional<Serie> serieBuscada;

    public Principal(String api_key, PeliculaRepository repository, UsuarioRepository usuarioRepository) {
        this.repositorio=repository;//puedo acceder a los metodos de mi interface repositorio.
        this.usuarioRepository=usuarioRepository; //para hacer el crud
        this.API_KEY=api_key;// y a los datos inyectados desde el springboot
    }

    public void muestraElMenu() {
        System.out.println(API_KEY);
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar peliculas 
                    2 - Opciones de administrador de usuario
                                
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarPelicula();
                    break;
                case 2:
                    administradorDeUsuario();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }


    private DatosPelicula getDatosPelicula() {
        System.out.println("Escribe el nombre de la Pelicula que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") +"&type=movie"+ API_KEY);
        System.out.println(json);
        DatosPelicula datos = conversor.obtenerDatos(json, DatosPelicula.class);
        return datos;
    }

    private void buscarPelicula() {
        DatosPelicula datos = getDatosPelicula();
        Pelicula peilcula = new Pelicula(datos);
        repositorio.save(peilcula);
        //datosSeries.add(datos);
        System.out.println(datos);
    }


    private void administradorDeUsuario() {
        int opc;
        var menu = """
                    1 - Crear Usuario
                    2 - Buscar Usuario
                    3 - Actualizar Usuario
                    4 - Eliminar Usuario
                              
                            
                    0 - Volver al menu anterior
                    """;
        System.out.println(menu);
        opc = teclado.nextInt();
        teclado.nextLine();
        switch (opc){
            case 1:
                crearUsuario();
                break;
            case 2:
                buscarUsuario();
                break;
            case 3:
                actualizarUsuario();
                break;
            case 4:
                eliminarUsuario();
                break;
            case 0:
                System.out.println("Cerrando la aplicación...");
                break;
            default:
                System.out.println("Opción inválida");

        }
    }
    private void crearUsuario() {
        //falta ppedir los datos
        Usuario usuario = new Usuario("camilo","camilo@gmail.com","1234");
        usuarioRepository.save(usuario);
    }
    private void buscarUsuario() {
        //recordar pasar a minuscula
        //si es diferente de falso entonces busque
        Optional<Usuario> usuario = usuarioRepository.findByNombre("camilo");
    }
    private void actualizarUsuario() {
        Usuario usuario = usuarioRepository.findByNombre("camilo").orElse(null);
        if(usuario != null || usuario.isActivo()!=false){
            usuario.setEmail("camiloActualizado@gmail.com");
            usuario.setContrasena("1111");
            usuarioRepository.save(usuario);
        }
    }
    private void eliminarUsuario() {
        Usuario usuario = usuarioRepository.findByNombre("camilo").orElse(null);
        if(usuario != null){
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }
    }




}

