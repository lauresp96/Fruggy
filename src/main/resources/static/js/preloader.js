document.addEventListener("DOMContentLoaded", function() {
    // Cuando el DOM está completamente cargado
    const preloader = document.getElementById('preloader');
    const content = document.getElementById('content');

    // Oculta el preloader y muestra el contenido de la página
    window.addEventListener('load', function() {
        preloader.style.display = 'none'; // Oculta el preloader
        content.style.display = 'block'; // Muestra el contenido
    });
});