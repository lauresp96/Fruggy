document.addEventListener('DOMContentLoaded', function () {
    let categoriaSelect = document.getElementById('categoria');
    let subcategoriaSelect = document.getElementById('subcategoria');

    // Función para obtener el idioma actual del navegador
    function getCurrentLanguage() {
        // Usa `navigator.language` para obtener el idioma del navegador
        let language = navigator.language || navigator.userLanguage || 'es';
        // Usa solo el primer idioma si es una lista (ej. 'es-ES' o 'en-US')
        return language.split('-')[0];
    }

    function actualizarSubcategorias() {
        let categoriaId = categoriaSelect.value;
        let idioma = getCurrentLanguage(); // Obtén el idioma actual

        if (!categoriaId) {
            subcategoriaSelect.innerHTML = '<option value="">Selecciona una subcategoría</option>';
            return;
        }

        // Define la URL de la API con el idioma actual
        const apiUrl = `/api/subcategorias?categoriaId=${categoriaId}&idioma=${idioma}`;

        // Realiza una solicitud GET
        fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Limpia las subcategorías actuales
                subcategoriaSelect.innerHTML = '<option value="">Selecciona una subcategoría</option>';

                // Añade nuevas subcategorías
                data.forEach(subcategoria => {
                    let option = document.createElement('option');
                    option.value = subcategoria.id;
                    // Usa el campo según el idioma seleccionado
                    option.textContent = idioma === 'en' ? subcategoria.tipo_en : subcategoria.tipo_es;
                    subcategoriaSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Evento cuando cambia la categoría
    categoriaSelect.addEventListener('change', actualizarSubcategorias);

    // Inicializar subcategorías en carga de la página si ya hay una categoría seleccionada
    actualizarSubcategorias();
});
