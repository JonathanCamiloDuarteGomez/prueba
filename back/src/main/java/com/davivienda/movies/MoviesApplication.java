package com.davivienda.movies;

import com.davivienda.movies.model.Usuario;
import com.davivienda.movies.principal.Principal;
import com.davivienda.movies.repositry.PeliculaRepository;
import com.davivienda.movies.repositry.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
http://127.0.0.1:5500, esta en CrosConfiguration que es el puerto del fornd

    JAva por defecto deja el puerto 8080 para servidor,
    entonces en caso que eramos cambiar vamos a applicacion.properties
    server.port=8081

 */
//@SpringBootApplication
//public class MoviesApplication {
//    //Las inyecciones de dependencias solo se puede llamar en una clase creada por el propio Spring
//
//    @Value("${API_KEY_OMDB:}")
//    private String API_KEY;
//
//    public static void main(String[] args) {
//        SpringApplication.run(MoviesApplication.class, args);
//    }
//
//
//}
@SpringBootApplication
public class MoviesApplication implements CommandLineRunner {
	//Las inyecciones de dependencias solo se puede llamar en una clase creada por el propio Spring
	@Autowired//Inyeccion de dependencias
	private PeliculaRepository repositoryPelicula;
	@Autowired
	private UsuarioRepository repositoryUsuario;
	@Value("${API_KEY_OMDB:}")
	private String API_KEY;

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(API_KEY,repositoryPelicula,repositoryUsuario);
		principal.muestraElMenu();

	}
}
