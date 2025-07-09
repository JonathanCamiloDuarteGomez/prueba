import getDatos from "./getDatos.js";

// Elementos del DOM
const elementos = {
    peliculas: document.querySelector('[data-name="peliculas"]'),
    categoria: document.querySelector('[data-name="categoria"]')
};

// Crear la lista de películas en la interfaz
function crearListaPeliculas(elemento, datos) {
    const ulExistente = elemento.querySelector('ul');
    if (ulExistente) {
        elemento.removeChild(ulExistente);
    }

    const ul = document.createElement('ul');
    ul.className = 'lista';

    if (!Array.isArray(datos)) {
        console.error("Los datos no son un array:", datos);
        return;
    }

    const listaHTML = datos.map((pelicula) => `
        <li>
            <a href="/detalles.html?id=${pelicula.id}">
                <img src="${pelicula.poster}" alt="${pelicula.titulo}">
            </a>
        </li>
    `).join('');

    ul.innerHTML = listaHTML;
    elemento.appendChild(ul);
}

// Cargar todas las películas del usuario autenticado
function cargarTodasLasPeliculas() {
    const nombreUsuario = localStorage.getItem('usuario');

    if (!nombreUsuario) {
        console.error("⚠ No hay usuario autenticado.");
        elementos.peliculas.innerHTML = "<p>Por favor inicia sesión para ver tus películas.</p>";
        return;
    }
    console.log(`Cargando películas para el usuario: ${nombreUsuario}`);    
    getDatos(`/api/peliculas/${nombreUsuario}`)
        
        .then(data => {
            crearListaPeliculas(elementos.peliculas, data);
        })
        .catch(error => {
            console.error(" Error al cargar las películas:", error);
        });
}

// Ejecutar al cargar el DOM
document.addEventListener("DOMContentLoaded", () => {
    cargarTodasLasPeliculas();
    configurarFiltroPorCategoria();
});

// Filtrado por categoría
function configurarFiltroPorCategoria() {
    const categoriaSelect = document.querySelector('[data-categorias]');
    const sectionsParaOcultar = document.querySelectorAll('.section');

    if (!categoriaSelect) return;

    categoriaSelect.addEventListener('change', function () {
        const categoriaSeleccionada = categoriaSelect.value;

        if (categoriaSeleccionada === 'todos') {
            sectionsParaOcultar.forEach(section => section.classList.remove('hidden'));
            elementos.categoria.classList.add('hidden');
        } else {
            sectionsParaOcultar.forEach(section => section.classList.add('hidden'));
            elementos.categoria.classList.remove('hidden');

            getDatos(`/api/categoria/${categoriaSeleccionada}`)
                .then(data => {
                    crearListaPeliculas(elementos.categoria, data);
                })
                .catch(error => {
                    console.error("Error al cargar los datos de la categoría.");
                });
        }
    });
}
//agregar peliculas 
document.getElementById('form-agregar-pelicula').addEventListener('submit', async function (e) {
    e.preventDefault();
    const input = document.getElementById('tituloPelicula');
    const titulo = input.value.trim();
    const usuario = localStorage.getItem('usuario');

    if (!usuario) {
        alert('Usuario no autenticado');
        return;
    }

    if (titulo.length < 2) {
        alert('Ingresa un título válido');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/peliculas/${usuario}?titulo=${encodeURIComponent(titulo)}`, {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error('No se pudo agregar la película');
        }

        const data = await response.json();
        alert(`Película "${data.titulo}" agregada exitosamente`);
        input.value = '';
        cargarTodasLasPeliculas(); // recarga la lista automáticamente
    } catch (error) {
        console.error('Error al agregar película:', error);
        alert('Error al agregar la película');
    }
});

