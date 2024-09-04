function toggleCategories() {
    var menu = document.getElementById('categoryMenu');
    // Cambia el estilo display entre 'none' y 'block'
    if (menu.style.display === 'none' || menu.style.display === '') {
        menu.style.display = 'block'; // Muestra el menú
    } else {
        menu.style.display = 'none'; // Oculta el menú
    }
}