package com.davivienda.movies.service;

import com.davivienda.movies.dto.PeliculaDTO;
import com.davivienda.movies.model.Categoria;
import com.davivienda.movies.model.DatosPelicula;
import com.davivienda.movies.model.Pelicula;
import com.davivienda.movies.model.Usuario;
import com.davivienda.movies.repositry.PeliculaRepository;
import com.davivienda.movies.repositry.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos convierteDatos;

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    @Value("${API_KEY_OMDB:}")
    private String API_KEY;

    @Autowired
    public PeliculaService(
            PeliculaRepository peliculaRepository,
            UsuarioRepository usuarioRepository,
            ConsumoAPI consumoAPI,
            ConvierteDatos convierteDatos
    ) {
        this.peliculaRepository = peliculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }

    // Buscar película en la API OMDb y guardarla asociada a un usuario
    public Optional<Pelicula> buscarYGuardarPelicula(String titulo, String nombreUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        String url = URL_BASE + titulo.replace(" ", "+") + "&type=movie" + API_KEY;
        String json = consumoAPI.obtenerDatos(url);

        DatosPelicula datos = convierteDatos.obtenerDatos(json, DatosPelicula.class);
        if (datos == null || datos.titulo() == null) {
            return Optional.empty();
        }

        Pelicula pelicula = new Pelicula(datos);
        pelicula.setUsuario(usuarioOpt.get());

        Pelicula guardada = peliculaRepository.save(pelicula);
        return Optional.of(guardada);
    }

    // Obtener todas las películas favoritas de un usuario
    public List<Pelicula> obtenerPeliculasPorUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombre(nombreUsuario)
                .map(usuario -> peliculaRepository.findByUsuarioId(usuario.getId()))
                .orElse(List.of());
    }


    // Eliminar una película por ID (solo si pertenece al usuario)
    public boolean eliminarPelicula(Long peliculaId, String nombreUsuario) {
        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(peliculaId);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombreUsuario);
        if (peliculaOpt.isPresent() && usuarioOpt.isPresent()) {
            Pelicula pelicula = peliculaOpt.get();
            if (pelicula.getUsuario().getId().equals(usuarioOpt.get().getId())) {
                peliculaRepository.deleteById(peliculaId);
                return true;
            }
        }
        return false;
    }
    public PeliculaDTO obtenerPorId(Long id) {
        Optional<Pelicula> pelicula = peliculaRepository.findAllById(id);
        if(pelicula.isPresent()){
            Pelicula p=pelicula.get();
            return new PeliculaDTO(p.getId(),p.getTitulo(),p.getAno_Streno(),p.getDirector()
                    ,p.getEvaluacion(),p.getPoster(),p.getGenero(),p.getActores(),p.getSinopsis());
        }else{
            return null;
        }
    }
    public List<PeliculaDTO> obtenerSeriesPorCategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromSpanol(nombreGenero);
        return convertirDatos(peliculaRepository.findByGenero(categoria));
    }

    // ===============================
    // CRUD de Usuario
    // ===============================

    public Usuario crearUsuario(String nombre, String email, String contrasena) {
        Usuario usuario = new Usuario(nombre, email, contrasena);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public Optional<Usuario> actualizarUsuario(String nombre, String nuevoEmail, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombre);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.isActivo()) {
                usuario.setEmail(nuevoEmail);
                usuario.setContrasena(nuevaContrasena);
                usuarioRepository.save(usuario);
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public boolean eliminarUsuario(String nombre) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombre(nombre);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false); // baja lógica
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    public List<PeliculaDTO> obtnerTodasLasPeliculas() {
        return convertirDatos(peliculaRepository.findAll());
    }

    private List<PeliculaDTO> convertirDatos(List<Pelicula> all) {
        return all.stream()
                .map(p->new PeliculaDTO(p.getId(),p.getTitulo(),p.getAno_Streno(),p.getDirector()
                        ,p.getEvaluacion(),p.getPoster(),p.getGenero(),p.getActores(),p.getSinopsis()))
                .collect(Collectors.toList());
    }


}
