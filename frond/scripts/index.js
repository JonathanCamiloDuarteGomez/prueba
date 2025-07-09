import getDatos from "./getDatos.js";

const elementos = {
    peliculas: document.querySelector('[data-name="peliculas"]')
};

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

function cargarTodasLasPeliculas() {
    getDatos("/api")
        .then(data => {
            crearListaPeliculas(elementos.peliculas, data);
        })
        .catch(error => {
            console.error("Error al cargar las películas:", error);
        });
}

document.addEventListener("DOMContentLoaded", () => {
    cargarTodasLasPeliculas();
});

// Filtrado por categoría
const categoriaSelect = document.querySelector('[data-categorias]');
const sectionsParaOcultar = document.querySelectorAll('.section');
const seccionCategoria = document.querySelector('[data-name="categoria"]');

categoriaSelect.addEventListener('change', function () {
    const categoriaSeleccionada = categoriaSelect.value;

    if (categoriaSeleccionada === 'todos') {
        sectionsParaOcultar.forEach(section => section.classList.remove('hidden'));
        seccionCategoria.classList.add('hidden');
    } else {
        sectionsParaOcultar.forEach(section => section.classList.add('hidden'));
        seccionCategoria.classList.remove('hidden');

        getDatos(`/api/categoria/${categoriaSeleccionada}`)
            .then(data => {
                crearListaPeliculas(seccionCategoria, data);
            })
            .catch(error => {
                console.error("Ocurrió un error al cargar los datos de la categoría.");
            });
    }
});
