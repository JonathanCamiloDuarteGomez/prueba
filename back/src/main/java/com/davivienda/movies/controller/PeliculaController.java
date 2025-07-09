package com.davivienda.movies.controller;

import com.davivienda.movies.dto.ActualizarUsuarioDTO;
import com.davivienda.movies.dto.CrearUsuarioDTO;
import com.davivienda.movies.dto.LoginDTO;
import com.davivienda.movies.dto.PeliculaDTO;
import com.davivienda.movies.model.Pelicula;
import com.davivienda.movies.model.Usuario;
import com.davivienda.movies.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    // ===========================
    // Endpoints de Principal
    // ===========================
    @GetMapping()
    public List<PeliculaDTO> obtenerTodasLasPeliculas(){ return peliculaService.obtnerTodasLasPeliculas();}

    // ===========================
    // Endpoints de Película
    // ===========================

    @PostMapping("/peliculas/{usuario}")
    public ResponseEntity<?> buscarYAgregarPelicula(@PathVariable String usuario, @RequestParam String titulo) {
        Optional<Pelicula> guardada = peliculaService.buscarYGuardarPelicula(titulo, usuario);
        return guardada.isPresent() ? ResponseEntity.ok(guardada.get())
                : ResponseEntity.badRequest().body("No se pudo guardar la película");
    }

    @GetMapping("/peliculas/{usuario}")
    public List<Pelicula> obtenerPeliculas(@PathVariable String usuario) {
        return peliculaService.obtenerPeliculasPorUsuario(usuario);
    }

    @DeleteMapping("/peliculas/{id}/{usuario}")
    public ResponseEntity<?> eliminarPelicula(@PathVariable Long id, @PathVariable String usuario) {
        boolean eliminado = peliculaService.eliminarPelicula(id, usuario);
        return eliminado ? ResponseEntity.ok("Película eliminada") : ResponseEntity.badRequest().body("No se pudo eliminar");
    }
    @GetMapping("/categoria/{nombreGenero}")
    public List<PeliculaDTO> obtenerPeliculasPorCategoria(@PathVariable String nombreGenero){
        return peliculaService.obtenerSeriesPorCategoria(nombreGenero);
    }
    @GetMapping("/{id}")
    //@path indica que este dato va a ir en la url
    public PeliculaDTO obtnerPorId(@PathVariable Long id){
        return peliculaService.obtenerPorId(id);
    }

    // ===========================
    // Endpoints de Usuario
    // ===========================


    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody @Valid CrearUsuarioDTO dto) {
        Usuario usuario = peliculaService.crearUsuario(dto.nombre(), dto.email(), dto.contrasena());
        return ResponseEntity.ok(usuario);
    }


    @PostMapping("/usuarios/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginDTO loginDTO) {
        return peliculaService.buscarUsuarioPorNombre(loginDTO.nombre())
                .map(usuario -> {
                    if (!usuario.isActivo()) {
                        return ResponseEntity.status(403).body("El usuario está inactivo");
                    }
                    if (!usuario.getContrasena().equals(loginDTO.contrasena())) {
                        return ResponseEntity.status(401).body("Contraseña incorrecta");
                    }

                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }


    @PutMapping("/usuarios/{nombre}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String nombre,
                                               @RequestBody ActualizarUsuarioDTO dto) {
        return peliculaService.actualizarUsuario(nombre, dto.email(), dto.contrasena())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("No se pudo actualizar el usuario"));
    }


    @DeleteMapping("/usuarios/{nombre}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String nombre) {
        return peliculaService.eliminarUsuario(nombre)
                ? ResponseEntity.ok("Usuario desactivado")
                : ResponseEntity.badRequest().body("No se pudo desactivar el usuario");
    }
}
