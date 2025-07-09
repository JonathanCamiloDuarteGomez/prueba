import getDatos from "./getDatos.js";

const params = new URLSearchParams(window.location.search);
const peliculaId = params.get('id');
const fichaDescripcion = document.getElementById('ficha-descripcion');




// Funcion para cargar informaciones de la serie
function cargarInfoSerie() {
    getDatos(`/api/${peliculaId}`)
        .then(data => {
            fichaDescripcion.innerHTML = `
                <img src="${data.poster}" alt="${data.titulo}" />
                <div>
                    <h2>${data.titulo}</h2>
                    <div class="descricao-texto">
                        <p><b>Fecha de lanzamiento:</b> ${data.ano_Estreno}</p>
                        <p><b>Director:</b> ${data.director}</p>
                        <p><b>Género:</b> ${data.genero}</p>
                        <p><b>Média de evaluaciones:</b> ${data.evaluacion}</p>
                        <p>${data.sinopsis}</p>
                        <p><b>Actores:</b> ${data.actores}</p>
                    </div>
                </div>
            `;
        })
        .catch(error => {
            console.error('Error al obtener informaciones de la serie:', error);
        });
}


// Carga las informaciones de la série y las temporadas cuando la página carga
cargarInfoSerie();

